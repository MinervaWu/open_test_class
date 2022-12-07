package com.example.demo.controller;

import com.example.demo.qo.BasePageQo;
import com.example.demo.security.MyUserDetails;
import com.example.demo.entity.Lesson;
import com.example.demo.enumeration.LessonStatusEnum;
import com.example.demo.qo.LessonQo;
import com.example.demo.service.LessonService;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.LessonVo;
import com.example.demo.vo.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Controller
@RequestMapping("/lesson")
public class LessonController {

    @Resource
    private LessonService lessonService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "lessonName", required = false, defaultValue = "") String lessonName,
                       @RequestParam(value = "teacherName", required = false, defaultValue = "") String teacherName) {
        LessonQo lessonQo = new LessonQo();
        lessonQo.setPageNum(pageNum);
        lessonQo.setPageSize(BasePageQo.NORMAL_SISE);
        lessonQo.setLessonStatus(LessonStatusEnum.ONLINE);
        lessonQo.setLessonName(lessonName);
        lessonQo.setTeacherName(teacherName);

        Result<?> result = lessonService.list(lessonQo);
        if (!result.isSuccess()) {
            return "/error";
        }

        //分页查询数据，每页显示5条，
        PageInfo<LessonVo> lessonVoPage = (PageInfo<LessonVo>) result.getData();
        model.addAttribute("page", lessonVoPage);
        return "/lesson/lessonManage";
    }

    @GetMapping("/addPage")
    public String addPage() {
        //跳转到lessonAdd.html界面
        return "/lesson/lessonAdd";
    }

    @PostMapping("/add")
    public String add(Lesson lesson, @AuthenticationPrincipal MyUserDetails userDetail) {
        if (!StringUtils.hasLength(lesson.getLessonName())
                || !StringUtils.hasLength(lesson.getTeacherName())
                || !StringUtils.hasLength(lesson.getInfo())) {
            return "/error";
        }
        lesson.setLessonStatus(LessonStatusEnum.ONLINE);

        LessonVo lessonVo = new LessonVo();
        lessonVo.setUserId(userDetail.getUserId());
        lessonVo.setLesson(lesson);

        lessonService.add(lessonVo);
        return "redirect:/lesson/list";
    }

    @GetMapping("/updatePage/{id}")
    public String updatePage(Model model, @PathVariable Integer id) {
        Result<Lesson> result = lessonService.getById(id);
        if (!result.isSuccess() || Objects.isNull(result.getData())) {
            return "/error";
        }

        Lesson lesson = result.getData();
        model.addAttribute("lesson", lesson);
        //跳转到update.html界面
        return "/lesson/lessonUpdate";
    }

    @PostMapping("/update")
    public String update(Lesson lesson, @AuthenticationPrincipal MyUserDetails userDetail) {
        if (NumberUtils.isNotPositive(lesson.getId())
                || !StringUtils.hasLength(lesson.getLessonName())
                || !StringUtils.hasLength(lesson.getTeacherName())) {
            return "/error";
        }
        LessonVo lessonVo = new LessonVo();
        lessonVo.setLesson(lesson);
        lessonVo.setUserId(userDetail.getUserId());

        lessonService.update(lessonVo);
        return "redirect:/lesson/list";
    }
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, @AuthenticationPrincipal MyUserDetails userDetail) {
        if (NumberUtils.isNotPositive(id)) {
            return "/error";
        }

        Lesson lesson = new Lesson();
        lesson.setId(id);

        LessonVo lessonVo = new LessonVo();
        lessonVo.setLesson(lesson);
        lessonVo.setUserId(userDetail.getUserId());

        lessonService.delete(lessonVo);

        return "redirect:/lesson/list";
    }
}
