package com.clemdrive.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clemdrive.file.api.IRecoveryFileService;
import com.clemdrive.file.component.FileDealComp;
import com.clemdrive.file.domain.RecoveryFile;
import com.clemdrive.file.domain.UserFile;
import com.clemdrive.file.io.DriveFile;
import com.clemdrive.file.mapper.RecoveryFileMapper;
import com.clemdrive.file.mapper.UserFileMapper;
import com.clemdrive.file.vo.file.RecoveryFileListVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RecoveryFileService extends ServiceImpl<RecoveryFileMapper, RecoveryFile> implements IRecoveryFileService {
    @Resource
    UserFileMapper userFileMapper;
    @Resource
    RecoveryFileMapper recoveryFileMapper;
    @Resource
    FileDealComp fileDealComp;


    @Override
    public void deleteUserFileByDeleteBatchNum(String deleteBatchNum) {


        LambdaQueryWrapper<UserFile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userFileLambdaQueryWrapper.eq(UserFile::getDeleteBatchNum, deleteBatchNum);
        userFileMapper.delete(userFileLambdaQueryWrapper);


    }

    @Override
    public void restorefile(String deleteBatchNum, String filePath, String sessionUserId) {

        List<UserFile> restoreUserFileList = userFileMapper.selectList(new QueryWrapper<UserFile>().lambda().eq(UserFile::getDeleteBatchNum, deleteBatchNum));
        for (UserFile restoreUserFile : restoreUserFileList) {
            restoreUserFile.setDeleteFlag(0);
            restoreUserFile.setDeleteBatchNum(deleteBatchNum);
            String fileName = fileDealComp.getRepeatFileName(restoreUserFile, restoreUserFile.getFilePath());
            if (restoreUserFile.isDirectory()) {
                if (!StringUtils.equals(fileName, restoreUserFile.getFileName())) {
                    userFileMapper.deleteById(restoreUserFile);
                } else {
                    userFileMapper.updateById(restoreUserFile);
                }
            } else if (restoreUserFile.isFile()) {
                restoreUserFile.setFileName(fileName);
                userFileMapper.updateById(restoreUserFile);
            }
        }

        DriveFile driveFile = new DriveFile(filePath, true);
        fileDealComp.restoreParentFilePath(driveFile, sessionUserId);

        LambdaQueryWrapper<RecoveryFile> recoveryFileServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recoveryFileServiceLambdaQueryWrapper.eq(RecoveryFile::getDeleteBatchNum, deleteBatchNum);
        recoveryFileMapper.delete(recoveryFileServiceLambdaQueryWrapper);
    }

    @Override
    public List<RecoveryFileListVo> selectRecoveryFileList(String userId) {
        return recoveryFileMapper.selectRecoveryFileList(userId);
    }
}
