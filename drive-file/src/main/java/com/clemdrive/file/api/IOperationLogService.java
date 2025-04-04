package com.clemdrive.file.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clemdrive.file.domain.OperationLogBean;

import java.util.List;

public interface IOperationLogService extends IService<OperationLogBean> {
    IPage<OperationLogBean> selectOperationLogPage(Integer current, Integer size);

    List<OperationLogBean> selectOperationLog();

    void insertOperationLog(OperationLogBean operationlogBean);
}
