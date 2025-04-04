package com.clemdrive.ufop.operation.upload.domain;

import com.clemdrive.ufop.constant.StorageTypeEnum;
import com.clemdrive.ufop.constant.UploadFileStatusEnum;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class UploadFileResult {
    private String fileName;
    private String extendName;
    private long fileSize;
    private String fileUrl;
    private String identifier;
    private StorageTypeEnum storageType;
    private UploadFileStatusEnum status;
    private BufferedImage bufferedImage;

}
