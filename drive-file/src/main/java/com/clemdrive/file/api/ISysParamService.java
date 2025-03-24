package com.clemdrive.file.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clemdrive.file.domain.SysParam;

public interface ISysParamService extends IService<SysParam> {
    String getValue(String key);
}
