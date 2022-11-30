package com.example.demo.entity;

import com.example.demo.enumeration.LessonStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Data
public class Lesson {
    private Integer id;
    private String lessonName;
    private String teacherName;
    private String info;
    private LessonStatusEnum lessonStatus;
    private Date createTime;
    private Integer createUserId;
    private Date updateTime;
    private Integer updateUserId;
}
