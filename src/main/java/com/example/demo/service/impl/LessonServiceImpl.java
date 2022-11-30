package com.example.demo.service.impl;

import com.example.demo.constant.ResultCode;
import com.example.demo.constant.ResultMessage;
import com.example.demo.dao.LessonDao;
import com.example.demo.dao.LessonSubscribeDao;
import com.example.demo.entity.Lesson;
import com.example.demo.entity.LessonSubscribe;
import com.example.demo.enumeration.LessonStatusEnum;
import com.example.demo.enumeration.SubscribeStatusEnum;
import com.example.demo.qo.LessonQo;
import com.example.demo.qo.LessonSubscribeQo;
import com.example.demo.service.LessonService;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.LessonVo;
import com.example.demo.vo.PageData;
import com.example.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Service
@Slf4j
public class LessonServiceImpl implements LessonService {
    @Resource
    private LessonDao lessonDao;

    @Resource
    private LessonSubscribeDao lessonSubscribeDao;

    @Override
    public Result<?> list(LessonQo lessonQo) {
        log.info("lessonQo | {}", lessonQo);

        PageData<Lesson> pageData = lessonDao.listPage(lessonQo);
        if (Objects.isNull(pageData) || CollectionUtils.isEmpty(pageData.getList())) {
            log.info("userList is empty");
            return Result.success(new PageData<>());
        }

        List<Lesson> lessonList = pageData.getList();
        log.info("lessonList | {}", lessonList.size());

        Map<Integer, Long> lessonSubscribeNumMap = getLessonSubscribeNumMap(lessonList);

        List<LessonVo> lessonVoList = new ArrayList<>(lessonList.size());
        for (Lesson lesson : lessonList) {
            Long subscribeNum = lessonSubscribeNumMap.get(lesson.getId());

            LessonVo lessonVo = new LessonVo();
            lessonVo.setSubscribeNum(NumberUtils.isNotPositive(subscribeNum) ? 0 : subscribeNum.intValue());
            lessonVo.setLesson(lesson);

            lessonVoList.add(lessonVo);
        }

        PageData<LessonVo> lessonVoPageData = new PageData<>();
        lessonVoPageData.setTotal(pageData.getTotal());
        lessonVoPageData.setList(lessonVoList);
        return Result.success(lessonVoPageData);
    }

    private Map<Integer, Long> getLessonSubscribeNumMap(List<Lesson> lessonList) {
        List<Integer> lessonIdList = lessonList.stream().map(Lesson::getId).collect(Collectors.toList());

        LessonSubscribeQo lessonSubscribeQo = new LessonSubscribeQo();
        lessonSubscribeQo.setLessonIdList(lessonIdList);
        lessonSubscribeQo.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE);
        List<LessonSubscribe> lessonSubscribeList = lessonSubscribeDao.listByQo(lessonSubscribeQo);
        if (CollectionUtils.isEmpty(lessonSubscribeList)) {
            log.info("lessonSubscribeList is empty");
            return Collections.EMPTY_MAP;
        }
        log.info("lessonSubscribeList | {}", lessonSubscribeList.size());

        Map<Integer, Long> result = lessonSubscribeList.stream().collect(Collectors.groupingBy(LessonSubscribe::getLessonId, Collectors.counting()));
        return result;
    }

    @Override
    public Result<?> add(LessonVo lessonVo) {
        log.info("lessonVo | {}", lessonVo);

        Lesson lesson = lessonVo.getLesson();
        boolean unique = checkUnique(lesson.getLessonName(), lesson.getTeacherName());
        if (!unique) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_EXIST_ERROR);
        }

        lesson.setCreateUserId(lessonVo.getUser().getId());
        lesson.setUpdateUserId(lessonVo.getUser().getId());
        Integer count = lessonDao.insert(lesson);
        log.info("count | {}", count);
        if (NumberUtils.isNotPositive(count)) {
            return Result.fail(ResultCode.RUN_ERROR, ResultMessage.DATA_BASE_RUN_ERROR);
        }

        return Result.success(lesson);
    }

    @Override
    public Result<?> delete(LessonVo lessonVo) {
        log.info("lessonVo | {}", lessonVo);

        Integer id = lessonVo.getLesson().getId();
        if (NumberUtils.isNotPositive(id)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }

        Lesson oldLesson = lessonDao.getById(id);
        log.info("oldLesson | {}", oldLesson);
        if (Objects.isNull(oldLesson)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_ERROR);
        }

        oldLesson.setLessonStatus(LessonStatusEnum.OFFLINE);
        oldLesson.setUpdateUserId(lessonVo.getUser().getId());
        lessonDao.update(oldLesson);
        log.info("oldLesson | {}", oldLesson);
        return Result.successWithoutData();
    }

    private boolean checkUnique(String lessonName, String teacherName) {
        LessonQo lessonQo = new LessonQo();
        lessonQo.setLessonName(lessonName);
        lessonQo.setTeacherName(teacherName);

        PageData<Lesson> pageData = lessonDao.listPage(lessonQo);
        if (Objects.isNull(pageData) || CollectionUtils.isEmpty(pageData.getList())) {
            return true;
        }
        return false;
    }

    @Override
    public Result<?> update(LessonVo lessonVo) {
        log.info("lessonVo | {}", lessonVo);
        Lesson lesson = lessonVo.getLesson();
        if (Objects.isNull(lesson) || NumberUtils.isNotPositive(lesson.getId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }

        Lesson oldLesson = lessonDao.getById(lesson.getId());
        log.info("oldLesson | {}", oldLesson);
        if (Objects.isNull(oldLesson)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_ERROR);
        }

        if (checkAndUpdateOldLesson(lesson, oldLesson)) {
            oldLesson.setUpdateUserId(lessonVo.getUser().getId());
            Integer updateCount = lessonDao.update(oldLesson);
            if (NumberUtils.isNotPositive(updateCount)) {
                return Result.fail(ResultCode.RUN_ERROR, ResultMessage.DATA_BASE_RUN_ERROR);
            }
        }
        return Result.success(oldLesson);
    }

    private boolean checkAndUpdateOldLesson(Lesson lesson, Lesson oldLesson) {
        log.info("lesson, oldLesson | {}, {}", lesson, oldLesson);

        boolean change = false;
        //如果新值非空且不等于旧值
        if (StringUtils.hasLength(lesson.getTeacherName()) && !lesson.getTeacherName().equals(oldLesson.getTeacherName())) {
            oldLesson.setTeacherName(lesson.getTeacherName());
            change = true;
        }
        if (StringUtils.hasLength(lesson.getInfo()) && !lesson.getInfo().equals(oldLesson.getInfo())) {
            oldLesson.setInfo(lesson.getInfo());
            change = true;
        }
        log.info("change | {}", change);
        return change;
    }
}
