package com.clemdrive.file.dto.commonfile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "共享文件DTO", required = true)
public class CommonFileDTO {
    @Schema(name = "用户文件id")
    private String userFileId;
    @Schema(name = "共享用户id集合")
    private String commonUserList;
}
