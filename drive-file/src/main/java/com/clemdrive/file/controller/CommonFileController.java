package com.clemdrive.file.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clemdrive.common.anno.MyLog;
import com.clemdrive.common.result.RestResult;
import com.clemdrive.common.util.security.JwtUser;
import com.clemdrive.common.util.security.SessionUtil;
import com.clemdrive.file.api.ICommonFileService;
import com.clemdrive.file.api.IFilePermissionService;
import com.clemdrive.file.api.IUserFileService;
import com.clemdrive.file.domain.CommonFile;
import com.clemdrive.file.domain.FilePermission;
import com.clemdrive.file.domain.UserFile;
import com.clemdrive.file.dto.commonfile.CommonFileDTO;
import com.clemdrive.file.io.DriveFile;
import com.clemdrive.file.vo.commonfile.CommonFileListVo;
import com.clemdrive.file.vo.commonfile.CommonFileUser;
import com.clemdrive.file.vo.file.FileListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "common", description = "该接口为文件共享接口")
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonFileController {

    public static final String CURRENT_MODULE = "文件共享";

    @Resource
    ICommonFileService commonFileService;
    @Resource
    IFilePermissionService filePermissionService;
    @Resource
    IUserFileService userFileService;

    @Operation(summary = "将文件共享给他人", description = "共享文件统一接口", tags = {"common"})
    @PostMapping(value = "/commonfile")
    @MyLog(operation = "共享文件", module = CURRENT_MODULE)
    @ResponseBody
    public RestResult<String> commonFile(@RequestBody CommonFileDTO commonFileDTO) {
        CommonFile commonFile = new CommonFile();
        commonFile.setUserFileId(commonFileDTO.getUserFileId());
        commonFile.setCommonFileId(IdUtil.getSnowflakeNextIdStr());

        commonFileService.save(commonFile);

        List<FilePermission> list = JSON.parseArray(commonFileDTO.getCommonUserList(), FilePermission.class);

        List<FilePermission> filePermissionList = new ArrayList<>();
        for (FilePermission filePermission : list) {
            filePermission.setFilePermissionId(IdUtil.getSnowflakeNextId());
            filePermission.setCommonFileId(commonFile.commonFileId);
            filePermissionList.add(filePermission);
        }
        filePermissionService.saveBatch(filePermissionList);

        return RestResult.success();
    }

    @Operation(summary = "获取共享空间的全量用户列表", description = "共享文件用户接口", tags = {"common"})
    @GetMapping(value = "/commonfileuser")
    @MyLog(operation = "共享文件用户", module = CURRENT_MODULE)
    @ResponseBody
    public RestResult<List<CommonFileUser>> commonFileUserList() {

        JwtUser sessionUserBean = SessionUtil.getSession();
        List<CommonFileUser> list = commonFileService.selectCommonFileUser(sessionUserBean.getUserId());
        return RestResult.success().data(list);
    }

    @Operation(summary = "获取共享用户文件列表", description = "用来做前台列表展示", tags = {"file"})
    @RequestMapping(value = "/getCommonFileByUser", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<CommonFileListVo> getCommonFileByUser(
            @Parameter(description = "用户id", required = true) String userId) {
        JwtUser sessionUserBean = SessionUtil.getSession();
        List<CommonFileListVo> commonFileVo = commonFileService.selectCommonFileByUser(userId, sessionUserBean.getUserId());

        return RestResult.success().data(commonFileVo);

    }

    @Operation(summary = "获取共享空间中某个用户的文件列表", description = "用来做前台列表展示", tags = {"file"})
    @RequestMapping(value = "/commonFileList", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<FileListVO> commonFileList(
            @Parameter(description = "用户id", required = true) Long commonFileId,
            @Parameter(description = "文件路径", required = true) String filePath,
            @Parameter(description = "当前页", required = true) long currentPage,
            @Parameter(description = "页面数量", required = true) long pageCount) {

        CommonFile commonFile = commonFileService.getById(commonFileId);
        UserFile userFile = userFileService.getById(commonFile.getUserFileId());
        DriveFile driveFile = new DriveFile(userFile.getFilePath(), filePath, true);
        IPage<FileListVO> fileList = userFileService.userFileList(userFile.getUserId(), driveFile.getPath(), currentPage, pageCount);

        return RestResult.success().data(fileList);

    }

}
