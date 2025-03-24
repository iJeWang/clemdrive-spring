package com.clemdrive.ufop.operation.write.product;

import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.clemdrive.ufop.operation.write.Writer;
import com.clemdrive.ufop.operation.write.domain.WriteFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

@Component
@Slf4j
public class FastDFSWriter extends Writer {
    @Resource
    AppendFileStorageClient defaultAppendFileStorageClient;

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {
        defaultAppendFileStorageClient.modifyFile("group1", writeFile.getFileUrl(), inputStream,
                writeFile.getFileSize(), 0);
    }
}
