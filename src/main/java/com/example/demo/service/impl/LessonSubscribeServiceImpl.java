package com.example.demo.service.impl;

import com.example.demo.constant.ResultCode;
import com.example.demo.constant.ResultMessage;
import com.example.demo.dao.LessonDao;
import com.example.demo.dao.LessonSubscribeDao;
import com.example.demo.entity.Lesson;
import com.example.demo.entity.LessonSubscribe;
import com.example.demo.entity.User;
import com.example.demo.enumeration.LessonStatusEnum;
import com.example.demo.enumeration.SubscribeStatusEnum;
import com.example.demo.qo.LessonQo;
import com.example.demo.qo.LessonSubscribeQo;
import com.example.demo.service.LessonSubscribeService;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.LessonSubscribeVo;
import com.example.demo.vo.LessonVo;
import com.example.demo.vo.PageData;
import com.example.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
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
        PageData<LessonSubscribe> pageData = lessonSubscribeDao.listPage(lessonSubscribeQo);

        if (Objects.isNull(pageData) || CollectionUtils.isEmpty(pageData.getList())) {
            log.info("userList is empty");
            return Result.success(new PageData<>());
        }

        List<LessonSubscribe> subscribeList = pageData.getList();
        log.info("subscribeList | {}", subscribeList.size());

        Map<Integer, Lesson> lessonVoMap = getLessonVoMap(subscribeList);

        List<LessonSubscribeVo> subscribeVoList = new ArrayList<>(subscribeList.size());
        for (LessonSubscribe subscribe : subscribeList) {
            Lesson lesson = lessonVoMap.get(subscribe.getLessonId());

            LessonSubscribeVo lessonSubscribeVo = new LessonSubscribeVo();
            lessonSubscribeVo.setLessonSubscribe(subscribe);
            lessonSubscribeVo.setLesson(lesson);
            subscribeVoList.add(lessonSubscribeVo);
        }

        PageData<LessonSubscribeVo> lessonVoPageData = new PageData<>();
        lessonVoPageData.setTotal(pageData.getTotal());
        lessonVoPageData.setList(subscribeVoList);
        return Result.success(lessonVoPageData);
    }

    private Map<Integer, Lesson> getLessonVoMap(List<LessonSubscribe> subscribeList) {
        List<Integer> lessonIdList = subscribeList.stream().map(LessonSubscribe::getLessonId).collect(Collectors.toList());

        LessonQo lessonQo = new LessonQo();
        lessonQo.setLessonIdList(lessonIdList);

        PageData<Lesson> lessonPageData = lessonDao.listPage(lessonQo);
        if (Objects.isNull(lessonPageData) || CollectionUtils.isEmpty(lessonPageData.getList())) {
            return Collections.EMPTY_MAP;
        }
        List<Lesson> lessonList = lessonPageData.getList();
        return lessonList.stream().collect(Collectors.toMap(Lesson::getId, Function.identity()));

    }

    @Override
    public Result<?> subscribe(LessonSubscribeVo lessonSubscribeVo) {
        User user = lessonSubscribeVo.getUser();

        LessonSubscribe lessonSubscribe = lessonSubscribeVo.getLessonSubscribe();
        lessonSubscribe.setUserId(user.getId());
        lessonSubscribe.setCreateUserId(user.getId());
        lessonSubscribe.setUpdateUserId(user.getId());
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
        User user = lessonSubscribeVo.getUser();

        Integer subscribeId = lessonSubscribeVo.getLessonSubscribe().getId();
        if (NumberUtils.isNotPositive(subscribeId)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }

        LessonSubscribe oldSubscribe = lessonSubscribeDao.getById(subscribeId);
        log.info("oldSubscribe | {}", oldSubscribe);
        if (Objects.isNull(oldSubscribe)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_ERROR);
        }
        oldSubscribe.setSubscribeStatus(SubscribeStatusEnum.DIS_SUBSCRIBE);
        oldSubscribe.setUpdateUserId(user.getId());

        lessonSubscribeDao.update(oldSubscribe);
        log.info("oldSubscribe | {}", oldSubscribe);
        return Result.successWithoutData();
    }
}
