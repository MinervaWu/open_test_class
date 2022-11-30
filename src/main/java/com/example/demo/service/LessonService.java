package com.example.demo.service;

import com.example.demo.entity.Lesson;
import com.example.demo.qo.LessonQo;
import com.example.demo.vo.LessonVo;
import com.example.demo.vo.Result;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface LessonService {
    Result<?> list(LessonQo lessonQo);

    Result<?> add(LessonVo lessonVo);

    Result<?> delete(LessonVo lessonVo);

    Result<?> update(LessonVo lessonVo);
}
