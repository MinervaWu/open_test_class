package com.example.demo.controller;

import com.example.demo.entity.LessonSubscribe;
import com.example.demo.enumeration.LessonStatusEnum;
import com.example.demo.enumeration.SubscribeStatusEnum;
import com.example.demo.qo.BasePageQo;
import com.example.demo.qo.LessonQo;
import com.example.demo.qo.LessonSubscribeQo;
import com.example.demo.security.MyUserDetails;
import com.example.demo.service.LessonSubscribeService;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.LessonSubscribeVo;
import com.example.demo.vo.LessonVo;
import com.example.demo.vo.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Controller
@RequestMapping("/lessonSubscribe")
public class LessonSubscribeController {

    @Resource
    private LessonSubscribeService lessonSubscribeService;

    @GetMapping("/lesson/list")
    public String list(Model model,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "lessonName", required = false, defaultValue = "") String lessonName,
                       @RequestParam(value = "teacherName", required = false, defaultValue = "") String teacherName,
                       @AuthenticationPrincipal MyUserDetails userDetail) {
        LessonQo lessonQo = new LessonQo();
        lessonQo.setPageNum(pageNum);
        lessonQo.setPageSize(BasePageQo.NORMAL_SISE);
        lessonQo.setLessonStatus(LessonStatusEnum.ONLINE);
        lessonQo.setUserId(userDetail.getUserId());
        lessonQo.setLessonName(lessonName);
        lessonQo.setTeacherName(teacherName);
        Result<?> result = lessonSubscribeService.listLesson(lessonQo);

        if (!result.isSuccess()) {
            return "/error";
        }

        //分页查询数据，每页显示5条，
        PageInfo<LessonVo> lessonVoPage = (PageInfo<LessonVo>) result.getData();
        model.addAttribute("page", lessonVoPage);
        return "/lesson/subscribe/lessonInfo";
    }

    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal MyUserDetails userDetail) {
        LessonSubscribeQo lessonSubscribeQo = new LessonSubscribeQo();
        lessonSubscribeQo.setUserId(userDetail.getUserId());
        lessonSubscribeQo.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE);
        lessonSubscribeQo.setPageSize(BasePageQo.NORMAL_SISE);
        Result<?> result = lessonSubscribeService.list(lessonSubscribeQo);
        if (!result.isSuccess()) {
            return "/error";
        }

        PageInfo<LessonVo> lessonVoPage = (PageInfo<LessonVo>) result.getData();
        model.addAttribute("page", lessonVoPage);
        return "/lesson/subscribe/subscribeManage";
    }

    @RequestMapping("/subscribe/{lessonId}")
    public String subscribe(@PathVariable Integer lessonId, @AuthenticationPrincipal MyUserDetails userDetail) {
        if (NumberUtils.isNotPositive(lessonId)) {
            return "/error";
        }

        LessonSubscribe lessonSubscribe = new LessonSubscribe();
        lessonSubscribe.setLessonId(lessonId);

        LessonSubscribeVo lessonSubscribeVo = new LessonSubscribeVo();
        lessonSubscribeVo.setLessonSubscribe(lessonSubscribe);
        lessonSubscribeVo.setUserId(userDetail.getUserId());

        lessonSubscribeService.subscribe(lessonSubscribeVo);
        return "redirect:/lessonSubscribe/list";
    }

    @RequestMapping("/cancelSubscribe/{id}")
    public String cancelSubscribe(@PathVariable Integer id, @AuthenticationPrincipal MyUserDetails userDetail) {
        if (NumberUtils.isNotPositive(id)) {
            return "/error";
        }

        LessonSubscribe lessonSubscribe = new LessonSubscribe();
        lessonSubscribe.setId(id);

        LessonSubscribeVo lessonSubscribeVo = new LessonSubscribeVo();
        lessonSubscribeVo.setLessonSubscribe(lessonSubscribe);
        lessonSubscribeVo.setUserId(userDetail.getUserId());

        lessonSubscribeService.cancelSubscribe(lessonSubscribeVo);
        return "redirect:/lessonSubscribe/list";
    }
}
