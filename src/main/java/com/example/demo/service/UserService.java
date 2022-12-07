package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.qo.UserQo;
import com.example.demo.vo.Result;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface UserService {
    Result<?> list(UserQo userQo);

    Result<?> add(User user);

    Result<?> delete(Integer id);

    Result<?> update(User user);

    Result<User> getById(Integer id);
}
