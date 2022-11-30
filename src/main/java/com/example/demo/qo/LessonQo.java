package com.example.demo.qo;

import com.example.demo.enumeration.LessonStatusEnum;
import lombok.Data;

import java.util.List;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Data
public class LessonQo extends BasePageQo{
    private Integer lessonId;
    private List<Integer> lessonIdList;
    private String lessonName;
    private String teacherName;
    private LessonStatusEnum lessonStatus;
}
