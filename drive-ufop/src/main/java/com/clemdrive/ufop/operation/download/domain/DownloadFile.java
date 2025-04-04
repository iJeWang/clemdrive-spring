package com.clemdrive.ufop.operation.download.domain;

import com.aliyun.oss.OSS;
import lombok.Data;

@Data
public class DownloadFile {
    private String fileUrl;
    private OSS ossClient;
    private Range range;
}
