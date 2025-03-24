package com.clemdrive.file.api;

import com.clemdrive.file.domain.FileBean;
import com.clemdrive.file.dto.file.DownloadFileDTO;
import com.clemdrive.file.dto.file.PreviewDTO;
import com.clemdrive.file.dto.file.UploadFileDTO;
import com.clemdrive.file.vo.file.UploadFileVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IFiletransferService {

    UploadFileVo uploadFileSpeed(UploadFileDTO uploadFileDTO);

    void uploadFile(HttpServletRequest request, UploadFileDTO UploadFileDto, String userId);

    void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO);

    void downloadUserFileList(HttpServletResponse httpServletResponse, String filePath, String fileName, List<String> userFileIds);

    void previewFile(HttpServletResponse httpServletResponse, PreviewDTO previewDTO);

    void previewPictureFile(HttpServletResponse httpServletResponse, PreviewDTO previewDTO);

    void deleteFile(FileBean fileBean);

    Long selectStorageSizeByUserId(String userId);
}
