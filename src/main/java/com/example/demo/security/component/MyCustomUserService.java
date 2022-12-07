package com.example.demo.security.component;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.qo.UserQo;
import com.example.demo.security.MyUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @author wuziwei
 * @description
 * @date 2022/12/5
 */
@Component
public class MyCustomUserService implements UserDetailsService {

    @Resource
    private UserDao userDao;
    /**
     * 登陆验证时，通过username获取用户的所有权限信息
     * 并返回UserDetails放到spring的全局缓存SecurityContextHolder中，以供授权器使用
     */
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        if (!StringUtils.hasLength(account)) {
            throw new RuntimeException("用户名不能为空！");
        }

        UserQo userQo = new UserQo();
        userQo.setAccount(account);
        User user = userDao.getByQo(userQo);

        if(Objects.isNull(user)){
            throw new RuntimeException("用户名不存在！");
        }

        MyUserDetails myUserDetail = new MyUserDetails();
        myUserDetail.setUsername(account);
        myUserDetail.setPassword(user.getPassword());
        myUserDetail.setUserId(user.getId());

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().getName());
        grantedAuthorities.add(grantedAuthority);
        myUserDetail.setAuthorities(grantedAuthorities);
        return myUserDetail;
    }
}
