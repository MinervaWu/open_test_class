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
public enum LessonStatusEnum {
    OFFLINE(0, "offline"),
    ONLINE(1, "online");

    @EnumValue
    private Integer id;
    private String name;
}
