package com.clemdrive.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clemdrive.file.domain.ShareFile;
import com.clemdrive.file.vo.share.ShareFileListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShareFileMapper extends BaseMapper<ShareFile> {
    List<ShareFileListVO> selectShareFileList(@Param("shareBatchNum") String shareBatchNum, @Param("shareFilePath") String filePath);
}
