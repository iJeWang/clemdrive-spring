package com.clemdrive.file.vo.commonfile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommonFileUser {
    @Schema(description = "用户id", example = "1")
    private long userId;
    @Schema(description = "用户名", example = "Clem网盘")
    private String username;
}
