package com.clemdrive.ufop.operation.read.product;

import com.qiniu.util.Auth;
import com.clemdrive.common.util.HttpsUtils;
import com.clemdrive.ufop.config.QiniuyunConfig;
import com.clemdrive.ufop.exception.operation.ReadException;
import com.clemdrive.ufop.operation.read.Reader;
import com.clemdrive.ufop.operation.read.domain.ReadFile;
import com.clemdrive.ufop.util.ReadFileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;

public class QiniuyunKodoReader extends Reader {

    private QiniuyunConfig qiniuyunConfig;

    public QiniuyunKodoReader() {

    }

    public QiniuyunKodoReader(QiniuyunConfig qiniuyunConfig) {
        this.qiniuyunConfig = qiniuyunConfig;
    }

    @Override
    public String read(ReadFile readFile) {
        String fileUrl = readFile.getFileUrl();
        String fileType = FilenameUtils.getExtension(fileUrl);
        try {
            return ReadFileUtils.getContentByInputStream(fileType, getInputStream(readFile.getFileUrl()));
        } catch (IOException e) {
            throw new ReadException("读取文件失败", e);
        }
    }

    public InputStream getInputStream(String fileUrl) {
        Auth auth = Auth.create(qiniuyunConfig.getKodo().getAccessKey(), qiniuyunConfig.getKodo().getSecretKey());

        String urlString = auth.privateDownloadUrl(qiniuyunConfig.getKodo().getDomain() + "/" + fileUrl);


        return HttpsUtils.doGet(urlString, null);
    }


}
