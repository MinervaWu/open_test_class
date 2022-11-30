package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.mybatis.pagehelper.annotation.SeedPage;
import com.example.demo.qo.UserQo;
import com.example.demo.vo.PageData;
import com.example.demo.vo.Result;

import java.util.List;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface UserDao {
    Integer update(User user);

    Integer insert(User user);

    @SeedPage
    PageData<User> listPage(UserQo userQo);

    User getById(Integer id);
}
