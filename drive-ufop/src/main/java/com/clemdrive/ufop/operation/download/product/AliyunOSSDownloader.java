package com.clemdrive.ufop.operation.download.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.clemdrive.ufop.config.AliyunConfig;
import com.clemdrive.ufop.operation.download.Downloader;
import com.clemdrive.ufop.operation.download.domain.DownloadFile;
import com.clemdrive.ufop.util.AliyunUtils;
import com.clemdrive.ufop.util.UFOPUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class AliyunOSSDownloader extends Downloader {

    private AliyunConfig aliyunConfig;

    public AliyunOSSDownloader() {

    }

    public AliyunOSSDownloader(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {

        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        OSSObject ossObject;
        if (downloadFile.getRange() != null) {
            GetObjectRequest getObjectRequest = new GetObjectRequest(aliyunConfig.getOss().getBucketName(),
                    UFOPUtils.getAliyunObjectNameByFileUrl(downloadFile.getFileUrl()));
            getObjectRequest.setRange(downloadFile.getRange().getStart(),
                    downloadFile.getRange().getStart() + downloadFile.getRange().getLength() - 1);
            ossObject = ossClient.getObject(getObjectRequest);
        } else {
            ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                    UFOPUtils.getAliyunObjectNameByFileUrl(downloadFile.getFileUrl()));
        }

        InputStream inputStream = ossObject.getObjectContent();

        downloadFile.setOssClient(ossClient);
        return inputStream;
    }


}
