package com.clemdrive.file.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clemdrive.common.result.RestResult;
import com.clemdrive.file.domain.user.Role;
import com.clemdrive.file.domain.user.UserBean;

import java.util.List;

public interface IUserService extends IService<UserBean> {


    String getUserIdByToken(String token);


    /**
     * 用户注册
     *
     * @param userBean 用户信息
     * @return 结果
     */
    RestResult<String> registerUser(UserBean userBean);


    UserBean findUserInfoByTelephone(String telephone);

    List<Role> selectRoleListByUserId(String userId);

    String getSaltByTelephone(String telephone);

    UserBean selectUserByTelephoneAndPassword(String username, String password);


}
