package com.clemdrive.ufop.operation.upload;

import com.clemdrive.ufop.exception.operation.UploadException;
import com.clemdrive.ufop.operation.upload.domain.UploadFile;
import com.clemdrive.ufop.operation.upload.domain.UploadFileResult;
import com.clemdrive.ufop.operation.upload.request.DriveMultipartFile;
import com.clemdrive.ufop.util.RedisUtil;
import com.clemdrive.ufop.util.concurrent.locks.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public abstract class Uploader {
    @Resource
    RedisLock redisLock;
    @Resource
    RedisUtil redisUtil;

    /**
     * 普通上传
     *
     * @param httpServletRequest http的request请求
     * @return 文件列表
     */
    public List<UploadFileResult> upload(HttpServletRequest httpServletRequest) {

        UploadFile uploadFile = new UploadFile();
        uploadFile.setChunkNumber(1);
        uploadFile.setChunkSize(0);
        uploadFile.setTotalChunks(1);
        uploadFile.setIdentifier(UUID.randomUUID().toString());

        return upload(httpServletRequest, uploadFile);
    }

    /**
     * 分片上传
     *
     * @param httpServletRequest http的request请求
     * @param uploadFile         分片上传参数
     * @return 文件列表
     */
    public List<UploadFileResult> upload(HttpServletRequest httpServletRequest, UploadFile uploadFile) {

        List<UploadFileResult> uploadFileResultList = new ArrayList<>();
        StandardMultipartHttpServletRequest request = (StandardMultipartHttpServletRequest) httpServletRequest;

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            throw new UploadException("未包含文件上传域");
        }

        try {

            Iterator<String> iter = request.getFileNames();
            while (iter.hasNext()) {
                List<MultipartFile> multipartFileList = request.getFiles(iter.next());
                for (MultipartFile multipartFile : multipartFileList) {
                    DriveMultipartFile driveMultipartFile = new DriveMultipartFile(multipartFile);
                    UploadFileResult uploadFileResult = doUploadFlow(driveMultipartFile, uploadFile);
                    uploadFileResultList.add(uploadFileResult);
                }
            }
        } catch (Exception e) {
            throw new UploadException(e);
        }

        return uploadFileResultList;
    }

    protected UploadFileResult doUploadFlow(DriveMultipartFile driveMultipartFile, UploadFile uploadFile) {

        UploadFileResult uploadFileResult;
        try {
            rectifier(driveMultipartFile, uploadFile);
            uploadFileResult = organizationalResults(driveMultipartFile, uploadFile);
        } catch (Exception e) {
            throw new UploadException(e);
        }

        return uploadFileResult;
    }

    /**
     * 取消上传
     *
     * @param uploadFile 分片上传参数
     */
    public abstract void cancelUpload(UploadFile uploadFile);

    protected abstract void doUploadFileChunk(DriveMultipartFile driveMultipartFile, UploadFile uploadFile) throws IOException;

    protected abstract UploadFileResult organizationalResults(DriveMultipartFile driveMultipartFile, UploadFile uploadFile);

    private void rectifier(DriveMultipartFile driveMultipartFile, UploadFile uploadFile) {
        String key = "DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":lock";
        String current_upload_chunk_number = "DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":current_upload_chunk_number";

        redisLock.lock(key);
        try {

            if (redisUtil.getObject(current_upload_chunk_number) == null) {
                redisUtil.set(current_upload_chunk_number, "1", 1000 * 60 * 60);
            }
            int currentUploadChunkNumber = Integer.parseInt(redisUtil.getObject(current_upload_chunk_number));

            if (uploadFile.getChunkNumber() != currentUploadChunkNumber) {
                redisLock.unlock(key);
                Thread.sleep(100);
                while (redisLock.tryLock(key, 300, TimeUnit.SECONDS)) {

                    currentUploadChunkNumber = Integer.parseInt(redisUtil.getObject(current_upload_chunk_number));

                    if (uploadFile.getChunkNumber() <= currentUploadChunkNumber) {
                        break;
                    } else {

                        if (Math.abs(currentUploadChunkNumber - uploadFile.getChunkNumber()) > 2) {
                            log.error("传入的切片数据异常，当前应上传切片为第{}块，传入的为第{}块。", currentUploadChunkNumber, uploadFile.getChunkNumber());
                            throw new UploadException("传入的切片数据异常");
                        }
                        redisLock.unlock(key);
                    }
                }
            }

            log.info("文件名{},正在上传第{}块, 共{}块>>>>>>>>>>", driveMultipartFile.getMultipartFile().getOriginalFilename(), uploadFile.getChunkNumber(), uploadFile.getTotalChunks());
            if (uploadFile.getChunkNumber() == currentUploadChunkNumber) {
                doUploadFileChunk(driveMultipartFile, uploadFile);
                log.info("文件名{},第{}块上传成功", driveMultipartFile.getMultipartFile().getOriginalFilename(), uploadFile.getChunkNumber());
                this.redisUtil.getIncr("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":current_upload_chunk_number");
            }
        } catch (Exception e) {
            log.error("第{}块上传失败，自动重试", uploadFile.getChunkNumber());
            redisUtil.set("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":current_upload_chunk_number", String.valueOf(uploadFile.getChunkNumber()), 1000 * 60 * 60);
            throw new UploadException("更新远程文件出错", e);
        } finally {

            redisLock.unlock(key);
        }

    }

    public synchronized boolean checkUploadStatus(UploadFile param, File confFile) throws IOException {
        RandomAccessFile confAccessFile = new RandomAccessFile(confFile, "rw");
        try {
            //设置文件长度
            confAccessFile.setLength(param.getTotalChunks());
            //设置起始偏移量
            confAccessFile.seek(param.getChunkNumber() - 1);
            //将指定的一个字节写入文件中 127，
            confAccessFile.write(Byte.MAX_VALUE);

        } finally {
            IOUtils.closeQuietly(confAccessFile);
        }
        byte[] completeStatusList = FileUtils.readFileToByteArray(confFile);
        //创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认的0,已上传的就是127
        for (byte b : completeStatusList) {
            if (b != Byte.MAX_VALUE) {
                return false;
            }
        }
        confFile.delete();
        return true;
    }

    public void writeByteDataToFile(byte[] fileData, File file, UploadFile uploadFile) {
        //第一步 打开将要写入的文件
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "rw");
            //第二步 打开通道
            FileChannel fileChannel = raf.getChannel();
            //第三步 计算偏移量
            long position = (uploadFile.getChunkNumber() - 1) * uploadFile.getChunkSize();
            //第四步 获取分片数据
//            byte[] fileData = DriveMultipartFile.getUploadBytes();
            //第五步 写入数据
            fileChannel.position(position);
            fileChannel.write(ByteBuffer.wrap(fileData));
            fileChannel.force(true);
            fileChannel.close();
            raf.close();
        } catch (IOException e) {
            throw new UploadException(e);
        }

    }


}
