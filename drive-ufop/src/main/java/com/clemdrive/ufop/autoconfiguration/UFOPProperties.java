package com.clemdrive.ufop.autoconfiguration;

import com.clemdrive.ufop.config.AliyunConfig;
import com.clemdrive.ufop.config.MinioConfig;
import com.clemdrive.ufop.config.QiniuyunConfig;
import com.clemdrive.ufop.domain.ThumbImage;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ufop")
public class UFOPProperties {

    private String bucketName;
    private String storageType;
    private String localStoragePath;
    private AliyunConfig aliyun = new AliyunConfig();
    private ThumbImage thumbImage = new ThumbImage();
    private MinioConfig minio = new MinioConfig();
    private QiniuyunConfig qiniuyun = new QiniuyunConfig();
}
