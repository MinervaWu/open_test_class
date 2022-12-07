package com.example.demo.dao;

import com.example.demo.entity.Lesson;
import com.example.demo.qo.LessonQo;

import java.util.List;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface LessonDao {

    List<Lesson> listByQo(LessonQo lessonQo);

    Integer insert(Lesson lesson);

    Integer update(Lesson lesson);

    Lesson getById (Integer id);
}
