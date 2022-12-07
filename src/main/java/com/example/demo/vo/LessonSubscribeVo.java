package com.example.demo.vo;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.LessonSubscribe;
import com.example.demo.entity.User;
import lombok.Data;

/**
 * @author wuziwei
 * @description
 * @date 2022/12/1
 */
@Data
public class LessonSubscribeVo {
    private Integer userId;
    private LessonSubscribe lessonSubscribe;
    private Lesson lesson;

}
