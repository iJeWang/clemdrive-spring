package com.clemdrive.file.component;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clemdrive.file.api.IFiletransferService;
import com.clemdrive.file.api.IRecoveryFileService;
import com.clemdrive.file.domain.FileBean;
import com.clemdrive.file.domain.UserFile;
import com.clemdrive.file.io.DriveFile;
import com.clemdrive.file.mapper.FileMapper;
import com.clemdrive.file.mapper.UserFileMapper;
import com.clemdrive.ufop.factory.UFOPFactory;
import com.clemdrive.ufop.operation.copy.domain.CopyFile;
import com.clemdrive.ufop.util.UFOPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * 功能描述：异步任务业务类（@Async也可添加在方法上）
 */
@Slf4j
@Component
@Async("asyncTaskExecutor")
public class AsyncTaskComp {

    @Resource
    IRecoveryFileService recoveryFileService;
    @Resource
    IFiletransferService filetransferService;
    @Resource
    UFOPFactory ufopFactory;
    @Resource
    UserFileMapper userFileMapper;
    @Resource
    FileMapper fileMapper;
    @Resource
    FileDealComp fileDealComp;

    @Value("${ufop.storage-type}")
    private Integer storageType;

    public Long getFilePointCount(String fileId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileId, fileId);
        return userFileMapper.selectCount(lambdaQueryWrapper);
    }

    public Future<String> deleteUserFile(String userFileId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        if (userFile.getIsDir() == 1) {
            LambdaQueryWrapper<UserFile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userFileLambdaQueryWrapper.eq(UserFile::getDeleteBatchNum, userFile.getDeleteBatchNum());
            List<UserFile> list = userFileMapper.selectList(userFileLambdaQueryWrapper);
            recoveryFileService.deleteUserFileByDeleteBatchNum(userFile.getDeleteBatchNum());
            for (UserFile userFileItem : list) {

                Long filePointCount = getFilePointCount(userFileItem.getFileId());

                if (filePointCount != null && filePointCount == 0 && userFileItem.getIsDir() == 0) {
                    FileBean fileBean = fileMapper.selectById(userFileItem.getFileId());
                    if (fileBean != null) {
                        try {
                            filetransferService.deleteFile(fileBean);
                            fileMapper.deleteById(fileBean.getFileId());
                        } catch (Exception e) {
                            log.error("删除本地文件失败：" + JSON.toJSONString(fileBean));
                        }
                    }


                }
            }
        } else {

            recoveryFileService.deleteUserFileByDeleteBatchNum(userFile.getDeleteBatchNum());
            Long filePointCount = getFilePointCount(userFile.getFileId());

            if (filePointCount != null && filePointCount == 0 && userFile.getIsDir() == 0) {
                FileBean fileBean = fileMapper.selectById(userFile.getFileId());
                try {
                    filetransferService.deleteFile(fileBean);
                    fileMapper.deleteById(fileBean.getFileId());
                } catch (Exception e) {
                    log.error("删除本地文件失败：" + JSON.toJSONString(fileBean));
                }
            }
        }

        return new AsyncResult<>("deleteUserFile");
    }

    public Future<String> checkESUserFileId(String userFileId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        if (userFile == null) {
            fileDealComp.deleteESByUserFileId(userFileId);
        }
        return new AsyncResult<>("checkUserFileId");
    }


    public Future<String> saveUnzipFile(UserFile userFile, FileBean fileBean, int unzipMode, String entryName, String filePath) {
        String unzipUrl = UFOPUtils.getTempFile(fileBean.getFileUrl()).getAbsolutePath().replace("." + userFile.getExtendName(), "");
        String totalFileUrl = unzipUrl + entryName;
        File currentFile = new File(totalFileUrl);

        String fileId = null;
        if (!currentFile.isDirectory()) {

            FileInputStream fis = null;
            String md5Str = UUID.randomUUID().toString();
            try {
                fis = new FileInputStream(currentFile);
                md5Str = DigestUtils.md5Hex(fis);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(fis);
            }

            FileInputStream fileInputStream = null;
            try {
                Map<String, Object> param = new HashMap<>();
                param.put("identifier", md5Str);
                List<FileBean> list = fileMapper.selectByMap(param);

                if (list != null && !list.isEmpty()) { //文件已存在
                    fileId = list.get(0).getFileId();
                } else { //文件不存在
                    fileInputStream = new FileInputStream(currentFile);
                    CopyFile createFile = new CopyFile();
                    createFile.setExtendName(FilenameUtils.getExtension(totalFileUrl));
                    String saveFileUrl = ufopFactory.getCopier().copy(fileInputStream, createFile);

                    FileBean tempFileBean = new FileBean(saveFileUrl, currentFile.length(), storageType, md5Str, userFile.getUserId());

                    fileMapper.insert(tempFileBean);
                    fileId = tempFileBean.getFileId();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(fileInputStream);
                System.gc();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentFile.delete();
            }


        }


        DriveFile driveFile = null;
        if (unzipMode == 0) {
            driveFile = new DriveFile(userFile.getFilePath(), entryName, currentFile.isDirectory());
        } else if (unzipMode == 1) {
            driveFile = new DriveFile(userFile.getFilePath() + "/" + userFile.getFileName(), entryName, currentFile.isDirectory());
        } else if (unzipMode == 2) {
            driveFile = new DriveFile(filePath, entryName, currentFile.isDirectory());
        }

        UserFile saveUserFile = new UserFile(driveFile, userFile.getUserId(), fileId);
        String fileName = fileDealComp.getRepeatFileName(saveUserFile, saveUserFile.getFilePath());

        if (saveUserFile.getIsDir() == 1 && !fileName.equals(saveUserFile.getFileName())) {
            //如果是目录，而且重复，什么也不做
        } else {
            saveUserFile.setFileName(fileName);
            userFileMapper.insert(saveUserFile);
        }
        fileDealComp.restoreParentFilePath(driveFile, userFile.getUserId());

        return new AsyncResult<>("saveUnzipFile");
    }


}