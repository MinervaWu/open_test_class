package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.enumeration.UserStatusEnum;
import com.example.demo.qo.BasePageQo;
import com.example.demo.qo.UserQo;
import com.example.demo.service.UserService;
import com.example.demo.util.Base64Util;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.Result;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/list")
    public String getList(Model model, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        UserQo userQo = new UserQo();
        userQo.setPageNum(pageNum);
        userQo.setPageSize(BasePageQo.NORMAL_SISE);
        userQo.setUserStatus(UserStatusEnum.VALID);
        log.info("userQo | {}", userQo);

        Result<?> result = userService.list(userQo);
        log.info("result | {}", result);
        if (!result.isSuccess()) {
            return "/error";
        }

        PageInfo<User> userPage = (PageInfo<User>) result.getData();
        if (CollectionUtils.isEmpty(userPage.getList())) {
            return "/error";
        }
        for (User user : userPage.getList()) {
            user.setPassword(Base64Util.decode(user.getPassword()));
        }
        model.addAttribute("page", userPage);
        return "/user/userManage";
    }

    @GetMapping("/addPage")
    public String addPage() {
        //跳转到userAdd.html界面
        return "/user/userAdd";
    }

    @PostMapping("/add")
    public String add(User user) {
        log.info("user | {}", user);
        if (!StringUtils.hasLength(user.getUserName())
                || !StringUtils.hasLength(user.getAccount())
                || !StringUtils.hasLength(user.getPassword())
                || Objects.isNull(user.getRole())) {
            return "/error";
        }
        user.setUserStatus(UserStatusEnum.VALID);
        user.setPassword(Base64Util.encode(user.getPassword()));

        userService.add(user);
        return "redirect:/user/list";
    }

    @GetMapping("/updatePage/{id}")
    public String updatePage(Model model, @PathVariable Integer id) {
        log.info("id | {}", id);
        Result<User> result = userService.getById(id);
        log.info("result | {}", result);
        if (!result.isSuccess() || Objects.isNull(result.getData())) {
            return "/error";
        }

        User user = result.getData();
        user.setPassword(Base64Util.decode(user.getPassword()));
        model.addAttribute("user", user);
        //跳转到update.html界面
        return "/user/userUpdate";
    }

    @PostMapping("/update")
    public String update(User user) {
        log.info("user | {}", user);
        if (NumberUtils.isNotPositive(user.getId())
                || !StringUtils.hasLength(user.getUserName())
                || !StringUtils.hasLength(user.getAccount())
                || !StringUtils.hasLength(user.getPassword())
                || Objects.isNull(user.getRole())) {
            return "/error";
        }

        user.setPassword(Base64Util.encode(user.getPassword()));
        userService.update(user);
        return "redirect:/user/list";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        log.info("id | {}", id);
        if (NumberUtils.isNotPositive(id)) {
            return "/error";
        }
        userService.delete(id);
        return "redirect:/user/list";
    }
}
