package com.example.demo.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    INVALID(0, "invalid"),
    VALID(1, "valid");

    @EnumValue
    private Integer id;
    private String name;
}
