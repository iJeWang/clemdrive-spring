package com.clemdrive.ufop.exception.operation;

import com.clemdrive.ufop.exception.UFOPException;

public class CopyException extends UFOPException {
    public CopyException(Throwable cause) {
        super("创建出现了异常", cause);
    }

    public CopyException(String message) {
        super(message);
    }

    public CopyException(String message, Throwable cause) {
        super(message, cause);
    }

}
