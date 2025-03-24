package com.clemdrive.ufop.operation.copy.product;

import com.aliyun.oss.OSS;
import com.clemdrive.ufop.config.AliyunConfig;
import com.clemdrive.ufop.operation.copy.Copier;
import com.clemdrive.ufop.operation.copy.domain.CopyFile;
import com.clemdrive.ufop.util.AliyunUtils;
import com.clemdrive.ufop.util.UFOPUtils;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.UUID;

public class AliyunOSSCopier extends Copier {

    private AliyunConfig aliyunConfig;

    public AliyunOSSCopier() {

    }

    public AliyunOSSCopier(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public String copy(InputStream inputStream, CopyFile copyFile) {
        String uuid = UUID.randomUUID().toString();
        String fileUrl = UFOPUtils.getUploadFileUrl(uuid, copyFile.getExtendName());
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        try {
            ossClient.putObject(aliyunConfig.getOss().getBucketName(), fileUrl, inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
            ossClient.shutdown();
        }
        return fileUrl;
    }

}
