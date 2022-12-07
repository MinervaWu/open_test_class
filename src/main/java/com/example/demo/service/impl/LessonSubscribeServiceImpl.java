package com.example.demo.service.impl;

import com.example.demo.constant.ResultCode;
import com.example.demo.constant.ResultMessage;
import com.example.demo.dao.LessonDao;
import com.example.demo.dao.LessonSubscribeDao;
import com.example.demo.entity.Lesson;
import com.example.demo.entity.LessonSubscribe;
import com.example.demo.enumeration.SubscribeStatusEnum;
import com.example.demo.qo.LessonQo;
import com.example.demo.qo.LessonSubscribeQo;
import com.example.demo.service.LessonSubscribeService;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.LessonSubscribeVo;
import com.example.demo.vo.LessonVo;
import com.example.demo.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Service
@Slf4j
public class LessonSubscribeServiceImpl implements LessonSubscribeService {

    @Resource
    private LessonSubscribeDao lessonSubscribeDao;

    @Resource
    private LessonDao lessonDao;

    @Override
    public Result<?> list(LessonSubscribeQo lessonSubscribeQo) {
        log.info("lessonSubscribeQo | {}", lessonSubscribeQo);
        PageHelper.startPage(lessonSubscribeQo.getPageNum(), lessonSubscribeQo.getPageSize());

        List<LessonSubscribe> subscribeList = lessonSubscribeDao.listByQo(lessonSubscribeQo);
        PageInfo<LessonSubscribe> pageData = new PageInfo<LessonSubscribe>(subscribeList, lessonSubscribeQo.getPageSize());

        if (Objects.isNull(pageData) || CollectionUtils.isEmpty(pageData.getList())) {
            log.info("lessonSubscribeList is empty");
            return Result.success(new PageInfo<LessonSubscribe>());
        }

        log.info("subscribeList | {}", subscribeList.size());

        Map<Integer, Lesson> lessonVoMap = getLessonVoMap(subscribeList);

        List<LessonVo> lessonVoList = new ArrayList<>(subscribeList.size());
        for (LessonSubscribe subscribe : subscribeList) {
            Lesson lesson = lessonVoMap.get(subscribe.getLessonId());

            LessonVo lessonVo = new LessonVo();
            lessonVo.setLessonSubscribe(subscribe);
            lessonVo.setLesson(lesson);
            lessonVoList.add(lessonVo);
        }

        PageInfo<LessonVo> lessonVoPageData = new PageInfo<>();
        BeanUtils.copyProperties(pageData, lessonVoPageData);
        lessonVoPageData.setList(lessonVoList);
        return Result.success(lessonVoPageData);
    }

    private Map<Integer, Lesson> getLessonVoMap(List<LessonSubscribe> subscribeList) {
        List<Integer> lessonIdList = subscribeList.stream().map(LessonSubscribe::getLessonId).collect(Collectors.toList());

        LessonQo lessonQo = new LessonQo();
        lessonQo.setLessonIdList(lessonIdList);

        log.info("lessonQo | {}", lessonQo);
        List<Lesson> lessonList = lessonDao.listByQo(lessonQo);
        log.info("lessonList | {}", lessonList);
        if (CollectionUtils.isEmpty(lessonList)) {
            return Collections.EMPTY_MAP;
        }

        return lessonList.stream().collect(Collectors.toMap(Lesson::getId, Function.identity()));
    }

    @Override
    public Result<?> subscribe(LessonSubscribeVo lessonSubscribeVo) {

        LessonSubscribe lessonSubscribe = lessonSubscribeVo.getLessonSubscribe();
        lessonSubscribe.setUserId(lessonSubscribeVo.getUserId());
        lessonSubscribe.setCreateUserId(lessonSubscribeVo.getUserId());
        lessonSubscribe.setUpdateUserId(lessonSubscribeVo.getUserId());
        lessonSubscribe.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE);

        boolean unique = checkUnique(lessonSubscribe.getLessonId(), lessonSubscribe.getUserId());
        if (!unique) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_EXIST_ERROR);
        }

        Integer count = lessonSubscribeDao.insert(lessonSubscribe);
        log.info("count | {}", count);
        if (NumberUtils.isNotPositive(count)) {
            return Result.fail(ResultCode.RUN_ERROR, ResultMessage.DATA_BASE_RUN_ERROR);
        }
        return Result.success(lessonSubscribe);
    }

    private boolean checkUnique(Integer lessonId, Integer userId) {
        LessonSubscribeQo lessonSubscribeQo = new LessonSubscribeQo();
        lessonSubscribeQo.setLessonId(lessonId);
        lessonSubscribeQo.setUserId(userId);
        lessonSubscribeQo.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE);

        List<LessonSubscribe> lessonSubscribeList = lessonSubscribeDao.listByQo(lessonSubscribeQo);
        if (CollectionUtils.isEmpty(lessonSubscribeList)) {
            return true;
        }
        return false;
    }

    @Override
    public Result<?> cancelSubscribe(LessonSubscribeVo lessonSubscribeVo) {
        log.info("lessonSubscribeVo | {}", lessonSubscribeVo);
        Integer subscribeId = lessonSubscribeVo.getLessonSubscribe().getId();
        if (NumberUtils.isNotPositive(subscribeId)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }

        LessonSubscribe oldSubscribe = lessonSubscribeDao.getById(subscribeId);
        log.info("oldSubscribe | {}", oldSubscribe);
        if (Objects.isNull(oldSubscribe)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_ERROR);
        }
        oldSubscribe.setSubscribeStatus(SubscribeStatusEnum.CANCEL_SUBSCRIBE);
        oldSubscribe.setUpdateUserId(lessonSubscribeVo.getUserId());

        lessonSubscribeDao.update(oldSubscribe);
        log.info("oldSubscribe | {}", oldSubscribe);
        return Result.successWithoutData();
    }

    @Override
    public Result<?> listLesson(LessonQo lessonQo) {
        log.info("lessonQo | {}", lessonQo);

        PageHelper.startPage(lessonQo.getPageNum(), lessonQo.getPageSize());
        List<Lesson> lessonList = lessonDao.listByQo(lessonQo);

        PageInfo<Lesson> pageData = new PageInfo<Lesson>(lessonList, lessonQo.getPageSize());
        if (Objects.isNull(pageData) || CollectionUtils.isEmpty(pageData.getList())) {
            log.info("lessonList is empty");
            return Result.success(new PageInfo<LessonVo>());
        }

        log.info("lessonList | {}", pageData.getList().size());

        Map<Integer, LessonSubscribe> lessonSubscribeMap = getLessonSubscribeMap(lessonQo, pageData.getList());

        List<LessonVo> lessonVoList = new ArrayList<>(pageData.getList().size());
        for (Lesson lesson : pageData.getList()) {
            LessonVo lessonVo = new LessonVo();
            lessonVo.setLesson(lesson);

            if (lessonSubscribeMap.containsKey(lesson.getId())) {
                lessonVo.setSubscribe(true);
            } else {
                lessonVo.setSubscribe(false);
            }
            lessonVoList.add(lessonVo);
        }

        PageInfo<LessonVo> lessonVoPageData = new PageInfo<>();
        BeanUtils.copyProperties(pageData, lessonVoPageData);
        lessonVoPageData.setList(lessonVoList);

        return Result.success(lessonVoPageData);
    }


    private Map<Integer, LessonSubscribe> getLessonSubscribeMap(LessonQo lessonQo, List<Lesson> lessonList) {
        List<Integer> lessonIdList = lessonList.stream().map(Lesson::getId).collect(Collectors.toList());

        LessonSubscribeQo lessonSubscribeQo = new LessonSubscribeQo();
        lessonSubscribeQo.setLessonIdList(lessonIdList);
        lessonSubscribeQo.setUserId(lessonQo.getUserId());
        lessonSubscribeQo.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE);

        List<LessonSubscribe> lessonSubscribeList = lessonSubscribeDao.listByQo(lessonSubscribeQo);
        if (CollectionUtils.isEmpty(lessonSubscribeList)) {
            return Collections.EMPTY_MAP;
        }

        Map<Integer, LessonSubscribe> lessonSubscribeMap = lessonSubscribeList.stream().collect(Collectors.toMap(LessonSubscribe::getLessonId, Function.identity()));
        return lessonSubscribeMap;
    }
}
