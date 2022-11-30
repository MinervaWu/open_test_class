package com.example.demo.entity;

import com.example.demo.enumeration.UserStatusEnum;
import com.example.demo.enumeration.UserTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Data
public class User {
    private Integer id;
    private String userName;
    private String account;
    private String password;
    private UserTypeEnum userType;
    private UserStatusEnum userStatus;
    private Date createTime;
    private Date updateTime;
}
