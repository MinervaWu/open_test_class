package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.qo.UserQo;

import java.util.List;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
public interface UserDao {
    Integer update(User user);

    Integer insert(User user);

    List<User> listByQo(UserQo userQo);

    User getById(Integer id);

    User getByQo(UserQo userQo);
}
