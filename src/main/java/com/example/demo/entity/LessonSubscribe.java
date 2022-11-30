package com.example.demo.entity;

import com.example.demo.enumeration.SubscribeStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Data
public class LessonSubscribe {
    private Integer id;
    private Integer lessonId;
    private Integer userId;
    private SubscribeStatusEnum subscribeStatus;
    private Date createTime;
    private Integer createUserId;
    private Date updateTime;
    private Integer updateUserId;
}
