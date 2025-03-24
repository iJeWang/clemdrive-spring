package com.clemdrive.file.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clemdrive.common.exception.DriveException;
import com.clemdrive.common.operation.FileOperation;
import com.clemdrive.common.util.DateUtil;
import com.clemdrive.common.util.security.SessionUtil;
import com.clemdrive.file.api.IFileService;
import com.clemdrive.file.component.AsyncTaskComp;
import com.clemdrive.file.component.FileDealComp;
import com.clemdrive.file.domain.FileBean;
import com.clemdrive.file.domain.Image;
import com.clemdrive.file.domain.Music;
import com.clemdrive.file.domain.UserFile;
import com.clemdrive.file.mapper.FileMapper;
import com.clemdrive.file.mapper.ImageMapper;
import com.clemdrive.file.mapper.MusicMapper;
import com.clemdrive.file.mapper.UserFileMapper;
import com.clemdrive.file.util.DriveFileUtil;
import com.clemdrive.file.vo.file.FileDetailVO;
import com.clemdrive.ufop.factory.UFOPFactory;
import com.clemdrive.ufop.operation.download.Downloader;
import com.clemdrive.ufop.operation.download.domain.DownloadFile;
import com.clemdrive.ufop.util.UFOPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileService extends ServiceImpl<FileMapper, FileBean> implements IFileService {
    public static Executor executor = Executors.newFixedThreadPool(20);
    @Resource
    FileMapper fileMapper;
    @Resource
    UserFileMapper userFileMapper;
    @Resource
    UFOPFactory ufopFactory;

    @Value("${ufop.storage-type}")
    private Integer storageType;

    @Resource
    AsyncTaskComp asyncTaskComp;
    @Resource
    MusicMapper musicMapper;
    @Resource
    ImageMapper imageMapper;
    @Resource
    FileDealComp fileDealComp;

    @Override
    public Long getFilePointCount(String fileId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileId, fileId);
        long count = userFileMapper.selectCount(lambdaQueryWrapper);
        return count;
    }

    @Override
    public void unzipFile(String userFileId, int unzipMode, String filePath) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        FileBean fileBean = fileMapper.selectById(userFile.getFileId());
        File destFile = new File(UFOPUtils.getStaticPath() + "temp" + File.separator + fileBean.getFileUrl());


        Downloader downloader = ufopFactory.getDownloader(fileBean.getStorageType());
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.setFileUrl(fileBean.getFileUrl());
        InputStream inputStream = downloader.getInputStream(downloadFile);

        try {
            FileUtils.copyInputStreamToFile(inputStream, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String extendName = userFile.getExtendName();

        String unzipUrl = UFOPUtils.getTempFile(fileBean.getFileUrl()).getAbsolutePath().replace("." + extendName, "");

        List<String> fileEntryNameList = new ArrayList<>();

        try {
            fileEntryNameList = FileOperation.unzip(destFile, unzipUrl);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解压失败" + e);
            throw new DriveException(500001, "解压异常：" + e.getMessage());
        }

        if (destFile.exists()) {
            destFile.delete();
        }

        if (!fileEntryNameList.isEmpty() && unzipMode == 1) {
            UserFile driveDir = DriveFileUtil.getDriveDir(userFile.getUserId(), userFile.getFilePath(), userFile.getFileName());
            userFileMapper.insert(driveDir);
        }
        for (int i = 0; i < fileEntryNameList.size(); i++) {
            String entryName = fileEntryNameList.get(i);
            asyncTaskComp.saveUnzipFile(userFile, fileBean, unzipMode, entryName, filePath);

        }
    }

    @Override
    public void updateFileDetail(String userFileId, String identifier, long fileSize) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        String currentTime = DateUtil.getCurrentTime();
        FileBean fileBean = new FileBean();
        fileBean.setIdentifier(identifier);
        fileBean.setFileSize(fileSize);
        fileBean.setModifyTime(currentTime);
        fileBean.setModifyUserId(SessionUtil.getUserId());
        fileBean.setFileId(userFile.getFileId());
        fileMapper.updateById(fileBean);
        userFile.setUploadTime(currentTime);
        userFile.setModifyTime(currentTime);
        userFile.setModifyUserId(SessionUtil.getUserId());
        userFileMapper.updateById(userFile);
    }

    @Override
    public FileDetailVO getFileDetail(String userFileId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        FileBean fileBean = fileMapper.selectById(userFile.getFileId());
        Music music = musicMapper.selectOne(new QueryWrapper<Music>().eq("fileId", userFile.getFileId()));
        Image image = imageMapper.selectOne(new QueryWrapper<Image>().eq("fileId", userFile.getFileId()));

        if ("mp3".equalsIgnoreCase(userFile.getExtendName()) || "flac".equalsIgnoreCase(userFile.getExtendName())) {
            if (music == null) {
                fileDealComp.parseMusicFile(userFile.getExtendName(), fileBean.getStorageType(), fileBean.getFileUrl(), fileBean.getFileId());
                music = musicMapper.selectOne(new QueryWrapper<Music>().eq("fileId", userFile.getFileId()));
            }
        }

        FileDetailVO fileDetailVO = new FileDetailVO();
        BeanUtil.copyProperties(userFile, fileDetailVO);
        BeanUtil.copyProperties(fileBean, fileDetailVO);
        fileDetailVO.setMusic(music);
        fileDetailVO.setImage(image);
        return fileDetailVO;
    }

}
