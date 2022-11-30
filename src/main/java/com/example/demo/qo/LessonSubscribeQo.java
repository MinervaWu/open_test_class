package com.example.demo.qo;

import com.example.demo.entity.User;
import com.example.demo.enumeration.SubscribeStatusEnum;
import lombok.Data;

import java.util.List;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Data
public class LessonSubscribeQo extends BasePageQo{

    private List<Integer> lessonIdList;
    private SubscribeStatusEnum subscribeStatus;
    private Integer userId;
    private Integer lessonId;
}
