package com.example.demo.dao;

import com.example.demo.entity.Lesson;
import com.example.demo.mybatis.pagehelper.annotation.SeedPage;
import com.example.demo.qo.LessonQo;
import com.example.demo.vo.PageData;
import com.example.demo.vo.Result;

import java.util.List;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface LessonDao {
    @SeedPage
    PageData<Lesson> listPage(LessonQo lessonQo);

    Integer insert(Lesson lesson);

    Integer update(Lesson lesson);

    Lesson getById (Integer id);
}
