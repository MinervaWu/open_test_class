package com.example.demo.service;

import com.example.demo.qo.LessonSubscribeQo;
import com.example.demo.vo.LessonSubscribeVo;
import com.example.demo.vo.Result;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface LessonSubscribeService {
    Result<?> list(LessonSubscribeQo lessonSubscribeQo);

    Result<?> subscribe(LessonSubscribeVo lessonSubscribeVo);

    Result<?> cancelSubscribe(LessonSubscribeVo lessonSubscribeVo);
}
