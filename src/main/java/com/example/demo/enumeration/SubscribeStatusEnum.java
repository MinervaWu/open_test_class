package com.example.demo.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Getter
@AllArgsConstructor
public enum SubscribeStatusEnum {
    CANCEL_SUBSCRIBE(0, "cancelSubscribe"),
    SUBSCRIBE(1, "subscribe");

    @EnumValue
    private Integer id;
    private String name;
}
