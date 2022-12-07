package com.example.demo.service.impl;

import com.example.demo.constant.ResultCode;
import com.example.demo.constant.ResultMessage;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.enumeration.UserStatusEnum;
import com.example.demo.qo.UserQo;
import com.example.demo.service.UserService;
import com.example.demo.util.NumberUtils;
import com.example.demo.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author wuziwei
 * @description
 * @date 2022/11/30
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public Result<?> list(UserQo userQo) {
        log.info("userQo | {}", userQo);

        PageHelper.startPage(userQo.getPageNum(), userQo.getPageSize());
        List<User> userList = userDao.listByQo(userQo);

        PageInfo<User> pageData = new PageInfo<User>(userList, userQo.getPageSize());

        if (Objects.isNull(pageData) || CollectionUtils.isEmpty(pageData.getList())) {
            log.info("userList is empty");
            return Result.successWithoutData();
        }
        log.info("userList | {}", pageData.getList().size());

        return Result.success(pageData);
    }

    @Override
    public Result<?> add(User user) {
        log.info("user | {}", user);
        boolean unique = checkUnique(user.getAccount());
        if (!unique) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_EXIST_ERROR);
        }

        Integer count = userDao.insert(user);
        log.info("count | {}", count);
        if (NumberUtils.isNotPositive(count)) {
            return Result.fail(ResultCode.RUN_ERROR, ResultMessage.DATA_BASE_RUN_ERROR);
        }

        return Result.success(user);
    }

    private boolean checkUnique(String account) {
        UserQo userQo = new UserQo();
        userQo.setAccount(account);
        userQo.setUserStatus(UserStatusEnum.VALID);
        log.info("userQo | {}", userQo);

        List<User> userList = userDao.listByQo(userQo);
        if (CollectionUtils.isEmpty(userList)) {
            return true;
        }
        return false;
    }

    @Override
    public Result<?> delete(Integer id) {
        log.info("id | {}", id);

        if (NumberUtils.isNotPositive(id)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }

        User oldUser = userDao.getById(id);
        log.info("oldUser | {}", oldUser);
        if (Objects.isNull(oldUser)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_ERROR);
        }

        oldUser.setUserStatus(UserStatusEnum.INVALID);
        userDao.update(oldUser);
        log.info("oldUser | {}", oldUser);
        return Result.successWithoutData();
    }

    @Override
    public Result<?> update(User user) {
        log.info("user | {}", user);
        if (Objects.isNull(user) || NumberUtils.isNotPositive(user.getId())) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }

        User oldUser = userDao.getById(user.getId());
        log.info("oldUser | {}", oldUser);
        if (Objects.isNull(oldUser)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.DATA_ERROR);
        }

        if (checkAndUpdateOldUser(user, oldUser)) {
            Integer updateCount = userDao.update(oldUser);
            if (NumberUtils.isNotPositive(updateCount)) {
                return Result.fail(ResultCode.RUN_ERROR, ResultMessage.DATA_BASE_RUN_ERROR);
            }
        }
        return Result.success(oldUser);
    }

    @Override
    public Result<User> getById(Integer id) {
        log.info("id | {}", id);
        if (NumberUtils.isNotPositive(id)) {
            return Result.fail(ResultCode.ARG_ERROR, ResultMessage.ARG_ERROR);
        }
        User user = userDao.getById(id);
        log.info("user | {}", user);
        return Result.success(user);
    }

    private boolean checkAndUpdateOldUser(User user, User oldUser) {
        log.info("user, oldUser | {}, {}", user, oldUser);

        boolean change = false;
        //如果新值非空且不等于旧值
        if (StringUtils.hasLength(user.getUserName()) && !user.getUserName().equals(oldUser.getUserName())) {
            oldUser.setUserName(user.getUserName());
            change = true;
        }
        if (StringUtils.hasLength(user.getAccount()) && !user.getAccount().equals(oldUser.getAccount())) {
            oldUser.setAccount(user.getAccount());
            change = true;
        }
        if (StringUtils.hasLength(user.getPassword()) && !user.getPassword().equals(oldUser.getPassword())) {
            oldUser.setPassword(user.getPassword());
            change = true;
        }
        if (!Objects.isNull(user.getRole()) && !user.getRole().equals(oldUser.getRole())) {
            oldUser.setRole(user.getRole());
            change = true;
        }
        if (!Objects.isNull(user.getUserStatus()) && !user.getUserStatus().equals(oldUser.getUserStatus())) {
            oldUser.setUserStatus(user.getUserStatus());
            change = true;
        }
        log.info("change | {}", change);
        return change;
    }
}
