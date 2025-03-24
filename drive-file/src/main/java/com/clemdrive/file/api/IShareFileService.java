package com.clemdrive.file.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clemdrive.file.domain.ShareFile;
import com.clemdrive.file.vo.share.ShareFileListVO;

import java.util.List;

public interface IShareFileService extends IService<ShareFile> {

    List<ShareFileListVO> selectShareFileList(String shareBatchNum, String filePath);
}
