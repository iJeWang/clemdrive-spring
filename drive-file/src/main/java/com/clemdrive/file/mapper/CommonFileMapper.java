package com.clemdrive.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clemdrive.file.domain.CommonFile;
import com.clemdrive.file.vo.commonfile.CommonFileListVo;
import com.clemdrive.file.vo.commonfile.CommonFileUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommonFileMapper extends BaseMapper<CommonFile> {
    List<CommonFileUser> selectCommonFileUser(@Param("userId") String userId);

    List<CommonFileListVo> selectCommonFileByUser(@Param("userId") String userId, @Param("sessionUserId") String sessionUserId);

}
