package com.example.demo.dao;

import com.example.demo.entity.LessonSubscribe;
import com.example.demo.qo.LessonSubscribeQo;

import java.util.List;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface LessonSubscribeDao {

   List<LessonSubscribe> listByQo(LessonSubscribeQo lessonSubscribeQo);

    Integer insert(LessonSubscribe lessonSubscribe);

    LessonSubscribe getById(Integer subscribeId);

    Integer update(LessonSubscribe oldSubscribe);
}
