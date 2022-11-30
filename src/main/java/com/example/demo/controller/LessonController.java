package com.example.demo.controller;

import com.example.demo.constant.ResultCode;
import com.example.demo.constant.ResultMessage;
import com.example.demo.entity.Lesson;
import com.example.demo.enumeration.LessonStatusEnum;
import com.example.demo.qo.LessonQo;
import com.example.demo.service.LessonService;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.LessonVo;
import com.example.demo.vo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@RestController
@RequestMapping("/lesson")
public class LessonController {

    @Resource
    private LessonService lessonService;

    @GetMapping("/list")
    public Result<?> list(LessonQo lessonQo) {

        lessonQo.setLessonStatus(LessonStatusEnum.ONLINE);
        return lessonService.list(lessonQo);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody LessonVo lessonVo) {
        if (Objects.isNull(lessonVo.getLesson())
                || Objects.isNull(lessonVo.getUser())
                || !StringUtils.hasLength(lessonVo.getLesson().getLessonName())
                || !StringUtils.hasLength(lessonVo.getLesson().getTeacherName())
                || !StringUtils.hasLength(lessonVo.getLesson().getInfo())
                || Objects.isNull(lessonVo.getLesson().getLessonStatus())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }
        return lessonService.add(lessonVo);
    }

    @PostMapping("/delete")
    public Result<?> delete(@RequestBody LessonVo lessonVo) {
        if (Objects.isNull(lessonVo.getLesson())
                || Objects.isNull(lessonVo.getUser())
                || NumberUtils.isNotPositive(lessonVo.getLesson().getId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }
        return lessonService.delete(lessonVo);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody LessonVo lessonVo) {
        if (Objects.isNull(lessonVo.getLesson())
                || Objects.isNull(lessonVo.getUser())
                || NumberUtils.isNotPositive(lessonVo.getLesson().getId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }
        return lessonService.update(lessonVo);
    }
}
