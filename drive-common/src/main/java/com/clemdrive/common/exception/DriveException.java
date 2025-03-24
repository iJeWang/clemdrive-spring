package com.clemdrive.common.exception;

import com.clemdrive.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * 自定义全局异常类
 */
@Data
public class DriveException extends RuntimeException {
    private Integer code;

    public DriveException(String message) {
        super(message);
        this.code = ResultCodeEnum.UNKNOWN_ERROR.getCode();
    }

    public DriveException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public DriveException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "DriveException{" + "code=" + code + ", message=" + this.getMessage() + '}';
    }
}