package com.example.demo.dao;

import com.example.demo.entity.Lesson;
import com.example.demo.entity.LessonSubscribe;
import com.example.demo.mybatis.pagehelper.annotation.SeedPage;
import com.example.demo.qo.LessonSubscribeQo;
import com.example.demo.vo.PageData;

import java.util.List;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface LessonSubscribeDao {

    @SeedPage
    PageData listPage(LessonSubscribeQo lessonSubscribeQo);
    List<LessonSubscribe> listByQo(LessonSubscribeQo lessonSubscribeQo);

    Integer insert(LessonSubscribe lessonSubscribe);

    LessonSubscribe getById(Integer subscribeId);

    Integer update(LessonSubscribe oldSubscribe);
}
