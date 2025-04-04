package com.clemdrive.file.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.clemdrive.file.api.IShareFileService;
import com.clemdrive.file.component.FileDealComp;
import com.clemdrive.file.domain.ShareFile;
import com.clemdrive.file.domain.UserFile;
import com.clemdrive.file.io.DriveFile;
import com.clemdrive.file.service.UserFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Controller
public class TaskController {

    @Resource
    UserFileService userFileService;
    @Resource
    FileDealComp fileDealComp;
    @Resource
    IShareFileService shareFileService;
    @Autowired
    private ElasticsearchClient elasticsearchClient;


    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void updateElasticSearch() {
        List<UserFile> userfileList = userFileService.list(new QueryWrapper<UserFile>().eq("deleteFlag", 0));
        for (int i = 0; i < userfileList.size(); i++) {
            try {

                DriveFile ufopFile = new DriveFile(userfileList.get(i).getFilePath(), userfileList.get(i).getFileName(), userfileList.get(i).getIsDir() == 1);
                fileDealComp.restoreParentFilePath(ufopFile, userfileList.get(i).getUserId());
                if (i % 1000 == 0 || i == userfileList.size() - 1) {
                    log.info("目录健康检查进度：" + (i + 1) + "/" + userfileList.size());
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        userfileList = userFileService.list(new QueryWrapper<UserFile>().eq("deleteFlag", 0));
        for (UserFile userFile : userfileList) {
            fileDealComp.uploadESByUserFileId(userFile.getUserFileId());
        }

    }

    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void updateFilePath() {
        List<UserFile> list = userFileService.list();
        for (UserFile userFile : list) {
            try {
                String path = DriveFile.formatPath(userFile.getFilePath());
                if (!userFile.getFilePath().equals(path)) {
                    userFile.setFilePath(path);
                    userFileService.updateById(userFile);
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void updateShareFilePath() {
        List<ShareFile> list = shareFileService.list();
        for (ShareFile shareFile : list) {
            try {
                String path = DriveFile.formatPath(shareFile.getShareFilePath());
                shareFile.setShareFilePath(path);
                shareFileService.updateById(shareFile);
            } catch (Exception e) {
                //ignore
            }
        }
    }
}
