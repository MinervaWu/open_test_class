package com.example.demo.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.example.demo.util.NumberUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN(0, "ROLE_admin"),
    STUDENT(1, "ROLE_student"),
    TEACHER(2, "ROLE_teacher");
    @EnumValue
    private Integer id;
    private String name;
}
