package com.clemdrive.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "userlogininfo")
@Entity
@TableName("userlogininfo")
public class UserLoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long userLoginId;
    @Column(columnDefinition = "varchar(30) comment '用户登录日期'")
    private String userloginDate;
    @Column(columnDefinition = "varchar(20) comment '用户id'")
    private String userId;
}
