package com.clemdrive.ufop.operation.preview.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.clemdrive.ufop.config.AliyunConfig;
import com.clemdrive.ufop.domain.ThumbImage;
import com.clemdrive.ufop.operation.preview.Previewer;
import com.clemdrive.ufop.operation.preview.domain.PreviewFile;
import com.clemdrive.ufop.util.AliyunUtils;
import com.clemdrive.ufop.util.UFOPUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class AliyunOSSPreviewer extends Previewer {


    private AliyunConfig aliyunConfig;

    public AliyunOSSPreviewer() {

    }

    public AliyunOSSPreviewer(AliyunConfig aliyunConfig, ThumbImage thumbImage) {
        this.aliyunConfig = aliyunConfig;
        setThumbImage(thumbImage);
    }


    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {
        OSS ossClient = AliyunUtils.getOSSClient(aliyunConfig);
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getOss().getBucketName(),
                UFOPUtils.getAliyunObjectNameByFileUrl(previewFile.getFileUrl()));
        InputStream inputStream = ossObject.getObjectContent();
        previewFile.setOssClient(ossClient);
        return inputStream;
    }

}
