package com.clemdrive.file.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clemdrive.file.domain.CommonFile;
import com.clemdrive.file.vo.commonfile.CommonFileListVo;
import com.clemdrive.file.vo.commonfile.CommonFileUser;

import java.util.List;

public interface ICommonFileService extends IService<CommonFile> {
    List<CommonFileUser> selectCommonFileUser(String userId);

    List<CommonFileListVo> selectCommonFileByUser(String userId, String sessionUserId);
}