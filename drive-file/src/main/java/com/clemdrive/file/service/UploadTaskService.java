package com.clemdrive.file.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clemdrive.file.api.IUploadTaskService;
import com.clemdrive.file.domain.UploadTask;
import com.clemdrive.file.mapper.UploadTaskMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadTaskService extends ServiceImpl<UploadTaskMapper, UploadTask> implements IUploadTaskService {


}
