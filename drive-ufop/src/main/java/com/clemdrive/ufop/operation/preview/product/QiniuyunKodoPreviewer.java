package com.clemdrive.ufop.operation.preview.product;

import com.qiniu.util.Auth;
import com.clemdrive.common.util.HttpsUtils;
import com.clemdrive.ufop.config.QiniuyunConfig;
import com.clemdrive.ufop.domain.ThumbImage;
import com.clemdrive.ufop.operation.preview.Previewer;
import com.clemdrive.ufop.operation.preview.domain.PreviewFile;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Getter
@Setter
@Slf4j
public class QiniuyunKodoPreviewer extends Previewer {


    private QiniuyunConfig qiniuyunConfig;

    public QiniuyunKodoPreviewer() {

    }

    public QiniuyunKodoPreviewer(QiniuyunConfig qiniuyunConfig, ThumbImage thumbImage) {
        this.qiniuyunConfig = qiniuyunConfig;
        setThumbImage(thumbImage);
    }


    @Override
    protected InputStream getInputStream(PreviewFile previewFile) {

        Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());

        String urlString = auth.privateDownloadUrl(qiniuyunConfig.getKodo().getDomain() + "/" + previewFile.getFileUrl());

        return HttpsUtils.doGet(urlString, null);
    }


}
