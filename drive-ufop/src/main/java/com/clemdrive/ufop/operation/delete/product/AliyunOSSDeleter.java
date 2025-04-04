package com.clemdrive.ufop.operation.delete.product;

import com.aliyun.oss.OSS;
import com.clemdrive.ufop.config.AliyunConfig;
import com.clemdrive.ufop.operation.delete.Deleter;
import com.clemdrive.ufop.operation.delete.domain.DeleteFile;
import com.clemdrive.ufop.util.AliyunUtils;
import com.clemdrive.ufop.util.UFOPUtils;


public class AliyunOSSDeleter extends Deleter {
    private AliyunConfig aliyunConfig;

    public AliyunOSSDeleter() {

    }

    public AliyunOSSDeleter(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public void delete(DeleteFile deleteFile) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        try {
            ossClient.deleteObject(aliyunConfig.getOss().getBucketName(), UFOPUtils.getAliyunObjectNameByFileUrl(deleteFile.getFileUrl()));
        } finally {
            ossClient.shutdown();
        }
        deleteCacheFile(deleteFile);
    }
}
