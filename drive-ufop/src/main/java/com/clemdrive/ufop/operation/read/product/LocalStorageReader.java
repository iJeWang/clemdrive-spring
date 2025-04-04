package com.clemdrive.ufop.operation.read.product;

import com.clemdrive.ufop.exception.operation.ReadException;
import com.clemdrive.ufop.operation.read.Reader;
import com.clemdrive.ufop.operation.read.domain.ReadFile;
import com.clemdrive.ufop.util.UFOPUtils;
import com.clemdrive.ufop.util.ReadFileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class LocalStorageReader extends Reader {
    @Override
    public String read(ReadFile readFile) {

        String fileContent;
        FileInputStream fileInputStream = null;
        try {
            String extendName = FilenameUtils.getExtension(readFile.getFileUrl());
            fileInputStream = new FileInputStream(UFOPUtils.getStaticPath() + readFile.getFileUrl());
            fileContent = ReadFileUtils.getContentByInputStream(extendName, fileInputStream);
        } catch (IOException e) {
            throw new ReadException("文件读取出现异常", e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        return fileContent;
    }
}
