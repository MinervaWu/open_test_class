package com.example.demo.controller;

import com.example.demo.constant.ResultCode;
import com.example.demo.constant.ResultMessage;
import com.example.demo.entity.User;
import com.example.demo.qo.UserQo;
import com.example.demo.service.UserService;
import com.example.demo.util.NumberUtils;
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
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/list")
    public Result<?> list(UserQo userQo) {
        return userService.list(userQo);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody User user) {
        if (!StringUtils.hasLength(user.getUserName())
                || !StringUtils.hasLength(user.getAccount())
                || !StringUtils.hasLength(user.getPassword())
                || Objects.isNull(user.getUserType())
                || Objects.isNull(user.getUserStatus())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }
        return userService.add(user);
    }

    @PostMapping("/delete")
    public Result<?> delete(@RequestBody User user) {
        if (NumberUtils.isNotPositive(user.getId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }
        return userService.delete(user);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody User user) {
        if (NumberUtils.isNotPositive(user.getId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }
        return userService.update(user);
    }
}
