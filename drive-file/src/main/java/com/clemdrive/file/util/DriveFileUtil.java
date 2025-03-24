package com.clemdrive.file.util;

import cn.hutool.core.util.IdUtil;
import com.clemdrive.common.util.DateUtil;
import com.clemdrive.common.util.security.SessionUtil;
import com.clemdrive.file.domain.UserFile;
import com.clemdrive.file.io.DriveFile;

public class DriveFileUtil {


    public static UserFile getDriveDir(String userId, String filePath, String fileName) {
        UserFile userFile = new UserFile();
        userFile.setUserFileId(IdUtil.getSnowflakeNextIdStr());
        userFile.setUserId(userId);
        userFile.setFileId(null);
        userFile.setFileName(fileName);
        userFile.setFilePath(DriveFile.formatPath(filePath));
        userFile.setExtendName(null);
        userFile.setIsDir(1);
        userFile.setUploadTime(DateUtil.getCurrentTime());
        userFile.setCreateUserId(SessionUtil.getUserId());
        userFile.setCreateTime(DateUtil.getCurrentTime());
        userFile.setDeleteFlag(0);
        userFile.setDeleteBatchNum(null);
        return userFile;
    }

    public static UserFile getDriveFile(String userId, String fileId, String filePath, String fileName, String extendName) {
        UserFile userFile = new UserFile();
        userFile.setUserFileId(IdUtil.getSnowflakeNextIdStr());
        userFile.setUserId(userId);
        userFile.setFileId(fileId);
        userFile.setFileName(fileName);
        userFile.setFilePath(DriveFile.formatPath(filePath));
        userFile.setExtendName(extendName);
        userFile.setIsDir(0);
        userFile.setUploadTime(DateUtil.getCurrentTime());
        userFile.setCreateTime(DateUtil.getCurrentTime());
        userFile.setCreateUserId(SessionUtil.getUserId());
        userFile.setDeleteFlag(0);
        userFile.setDeleteBatchNum(null);
        return userFile;
    }

    public static UserFile searchDriveFileParam(UserFile userFile) {
        UserFile param = new UserFile();
        param.setFilePath(DriveFile.formatPath(userFile.getFilePath()));
        param.setFileName(userFile.getFileName());
        param.setExtendName(userFile.getExtendName());
        param.setDeleteFlag(0);
        param.setUserId(userFile.getUserId());
        param.setIsDir(0);
        return param;
    }

    public static String formatLikePath(String filePath) {
        String newFilePath = filePath.replace("'", "\\'");
        newFilePath = newFilePath.replace("%", "\\%");
        newFilePath = newFilePath.replace("_", "\\_");
        return newFilePath;
    }

}
