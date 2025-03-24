package com.clemdrive.file.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clemdrive.file.domain.FileBean;
import com.clemdrive.file.vo.file.FileDetailVO;

public interface IFileService extends IService<FileBean> {

    Long getFilePointCount(String fileId);

    void unzipFile(String userFileId, int unzipMode, String filePath);

    void updateFileDetail(String userFileId, String identifier, long fileSize);

    FileDetailVO getFileDetail(String userFileId);

}
