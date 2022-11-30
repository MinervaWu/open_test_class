package com.example.demo.controller;

import com.example.demo.constant.ResultCode;
import com.example.demo.constant.ResultMessage;
import com.example.demo.enumeration.SubscribeStatusEnum;
import com.example.demo.qo.LessonSubscribeQo;
import com.example.demo.service.LessonSubscribeService;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.LessonSubscribeVo;
import com.example.demo.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@RestController
@RequestMapping("/lessonSubscribe")
public class LessonSubscribeController {

    @Resource
    private LessonSubscribeService lessonSubscribeService;

    @GetMapping("/list")
    public Result<?> listLesson(@RequestBody LessonSubscribeQo lessonSubscribeQo) {
        if (Objects.isNull(lessonSubscribeQo.getUserId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }

        lessonSubscribeQo.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE);
        return lessonSubscribeService.list(lessonSubscribeQo);
    }

    @PostMapping("/subscribe")
    public Result<?> subscribe(@RequestBody LessonSubscribeVo lessonSubscribeVo) {
        if (Objects.isNull(lessonSubscribeVo.getLessonSubscribe())
                || Objects.isNull(lessonSubscribeVo.getUser())
                || NumberUtils.isNotPositive(lessonSubscribeVo.getLessonSubscribe().getLessonId())
                || NumberUtils.isNotPositive(lessonSubscribeVo.getUser().getId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }

        return lessonSubscribeService.subscribe(lessonSubscribeVo);
    }

    @PostMapping("/cancelSubscribe")
    public Result<?> cancelSubscribe(@RequestBody LessonSubscribeVo lessonSubscribeVo) {
        if (Objects.isNull(lessonSubscribeVo.getLessonSubscribe())
                || Objects.isNull(lessonSubscribeVo.getUser())
                || NumberUtils.isNotPositive(lessonSubscribeVo.getLessonSubscribe().getId())
                || NumberUtils.isNotPositive(lessonSubscribeVo.getUser().getId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }
        return lessonSubscribeService.cancelSubscribe(lessonSubscribeVo);
    }
}
