package com.clemdrive.file.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "修改文件DTO", required = true)
public class UpdateFileDTO {
    @Schema(description = "用户文件id")
    private String userFileId;
    @Schema(description = "文件内容")
    private String fileContent;
}
