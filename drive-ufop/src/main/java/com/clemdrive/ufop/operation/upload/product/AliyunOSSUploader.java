package com.clemdrive.ufop.operation.upload.product;


import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.clemdrive.ufop.config.AliyunConfig;
import com.clemdrive.ufop.constant.StorageTypeEnum;
import com.clemdrive.ufop.constant.UploadFileStatusEnum;
import com.clemdrive.ufop.operation.upload.Uploader;
import com.clemdrive.ufop.operation.upload.domain.UploadFile;
import com.clemdrive.ufop.operation.upload.domain.UploadFileInfo;
import com.clemdrive.ufop.operation.upload.domain.UploadFileResult;
import com.clemdrive.ufop.operation.upload.request.DriveMultipartFile;
import com.clemdrive.ufop.util.AliyunUtils;
import com.clemdrive.ufop.util.RedisUtil;
import com.clemdrive.ufop.util.UFOPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class AliyunOSSUploader extends Uploader {

    @Resource
    RedisUtil redisUtil;

    private AliyunConfig aliyunConfig;

    public AliyunOSSUploader() {

    }

    public AliyunOSSUploader(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    /**
     * 将文件分块进行升序排序并执行文件上传。
     *
     * @param uploadFile 上传信息
     */
    private void completeMultipartUpload(UploadFile uploadFile) {

        List<PartETag> partETags = JSON.parseArray(redisUtil.getObject("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":partETags"), PartETag.class);

        partETags.sort(Comparator.comparingInt(PartETag::getPartNumber));

        UploadFileInfo uploadFileInfo = JSON.parseObject(redisUtil.getObject("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":uploadPartRequest"), UploadFileInfo.class);

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(aliyunConfig.getOss().getBucketName(),
                        uploadFileInfo.getKey(),
                        uploadFileInfo.getUploadId(),
                        partETags);
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        // 完成上传。
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        ossClient.shutdown();

    }

    /**
     * 取消上传
     */
    @Override
    public void cancelUpload(UploadFile uploadFile) {

        UploadFileInfo uploadFileInfo = JSON.parseObject(redisUtil.getObject("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":uploadPartRequest"), UploadFileInfo.class);

        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        AbortMultipartUploadRequest abortMultipartUploadRequest =
                new AbortMultipartUploadRequest(aliyunConfig.getOss().getBucketName(),
                        uploadFileInfo.getKey(),
                        uploadFileInfo.getUploadId());
        ossClient.abortMultipartUpload(abortMultipartUploadRequest);
        ossClient.shutdown();
    }

    @Override
    protected void doUploadFileChunk(DriveMultipartFile driveMultipartFile, UploadFile uploadFile) throws IOException {

        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        try {
            UploadFileInfo uploadFileInfo = JSON.parseObject(redisUtil.getObject("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":uploadPartRequest"), UploadFileInfo.class);
            String fileUrl = driveMultipartFile.getFileUrl();
            if (uploadFileInfo == null) {

                InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(aliyunConfig.getOss().getBucketName(), fileUrl);
                InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
                String uploadId = upresult.getUploadId();

                uploadFileInfo = new UploadFileInfo();
                uploadFileInfo.setBucketName(aliyunConfig.getOss().getBucketName());
                uploadFileInfo.setKey(fileUrl);
                uploadFileInfo.setUploadId(uploadId);

                redisUtil.set("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":uploadPartRequest", JSON.toJSONString(uploadFileInfo));

            }

            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(uploadFileInfo.getBucketName());
            uploadPartRequest.setKey(uploadFileInfo.getKey());
            uploadPartRequest.setUploadId(uploadFileInfo.getUploadId());
            uploadPartRequest.setInputStream(driveMultipartFile.getUploadInputStream());
            uploadPartRequest.setPartSize(driveMultipartFile.getSize());
            uploadPartRequest.setPartNumber(uploadFile.getChunkNumber());
            log.debug(JSON.toJSONString(uploadPartRequest));

            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);

            log.debug("上传结果：" + JSON.toJSONString(uploadPartResult));

            if (redisUtil.hasKey("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":partETags")) {
                List<PartETag> partETags = JSON.parseArray(redisUtil.getObject("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":partETags"), PartETag.class);
                partETags.add(uploadPartResult.getPartETag());
                redisUtil.set("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":partETags", JSON.toJSONString(partETags));
            } else {
                List<PartETag> partETags = new ArrayList<>();
                partETags.add(uploadPartResult.getPartETag());
                redisUtil.set("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":partETags", JSON.toJSONString(partETags));
            }
        } finally {
            ossClient.shutdown();
        }


    }

    @Override
    protected UploadFileResult organizationalResults(DriveMultipartFile driveMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        UploadFileInfo uploadFileInfo = JSON.parseObject(redisUtil.getObject("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":uploadPartRequest"), UploadFileInfo.class);

        uploadFileResult.setFileUrl(uploadFileInfo.getKey());
        uploadFileResult.setFileName(driveMultipartFile.getFileName());
        uploadFileResult.setExtendName(driveMultipartFile.getExtendName());
        uploadFileResult.setFileSize(uploadFile.getTotalSize());
        if (uploadFile.getTotalChunks() == 1) {
            uploadFileResult.setFileSize(driveMultipartFile.getSize());
        }
        uploadFileResult.setStorageType(StorageTypeEnum.ALIYUN_OSS);
        uploadFileResult.setIdentifier(uploadFile.getIdentifier());
        if (uploadFile.getChunkNumber() == uploadFile.getTotalChunks()) {
            log.info("分片上传完成");
            completeMultipartUpload(uploadFile);
            redisUtil.deleteKey("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":current_upload_chunk_number");
            redisUtil.deleteKey("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":partETags");
            redisUtil.deleteKey("DriveUploader:Identifier:" + uploadFile.getIdentifier() + ":uploadPartRequest");
            if (UFOPUtils.isImageFile(uploadFileResult.getExtendName())) {

                OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
                OSSObject ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                        UFOPUtils.getAliyunObjectNameByFileUrl(uploadFileResult.getFileUrl()));
                InputStream is = ossObject.getObjectContent();
                BufferedImage src;
                try {
                    src = ImageIO.read(is);
                    uploadFileResult.setBufferedImage(src);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(is);
                }

            }
            uploadFileResult.setStatus(UploadFileStatusEnum.SUCCESS);
        } else {
            uploadFileResult.setStatus(UploadFileStatusEnum.UNCOMPLATE);

        }
        return uploadFileResult;
    }


}
