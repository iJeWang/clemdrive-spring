package com.clemdrive.ufop.operation.write;

import com.clemdrive.ufop.operation.write.domain.WriteFile;

import java.io.InputStream;

public abstract class Writer {
    public abstract void write(InputStream inputStream, WriteFile writeFile);
}
