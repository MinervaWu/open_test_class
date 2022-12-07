package com.example.demo.qo;

import com.example.demo.enumeration.RoleEnum;
import com.example.demo.enumeration.UserStatusEnum;
import lombok.Data;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Data
public class UserQo extends BasePageQo{
    private Integer userId;
    private String account;
    private String password;
    private UserStatusEnum userStatus;
    private RoleEnum role;
    private String userName;
}
