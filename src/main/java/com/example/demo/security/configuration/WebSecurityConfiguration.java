package com.example.demo.security.configuration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import javax.annotation.Resource;

/**
 * @author wuziwei
 * @description
 * @date 2022/12/5
 */
@SpringBootConfiguration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private PasswordEncoder myPasswordEncoder;

    @Resource
    private UserDetailsService myCustomUserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http//使用form表单post方式进行登录
                .formLogin()
                .loginPage("/login")
                //设置登录成功跳转页面，error=true控制页面错误信息的展示
                .successForwardUrl("/index")
                .failureUrl("/login?error=true")
                .and()
                .logout().permitAll();


        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user/**").hasRole("admin")
                .antMatchers("/lesson/**").hasRole("teacher")
                .antMatchers("/lessonSubscribe/**").hasRole("student");

        //session管理,失效后跳转
        http.sessionManagement().invalidSessionUrl("/login");
        //单用户登录，如果有一个登录了，同一个用户在其他地方登录将前一个剔除下线
        //http.sessionManagement().maximumSessions(1).expiredSessionStrategy(expiredSessionStrategy());
        //单用户登录，如果有一个登录了，同一个用户在其他地方不能登录
        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
        //退出时情况cookies
        http.logout().deleteCookies("JESSIONID");
        //解决中文乱码问题
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8"); filter.setForceEncoding(true);
        //
        http.addFilterBefore(filter, CsrfFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        //对于在header里面增加token等类似情况，放行所有OPTIONS请求。
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myCustomUserService).passwordEncoder(myPasswordEncoder);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //对默认的UserDetailsService进行覆盖
        authenticationProvider.setUserDetailsService(myCustomUserService);
        authenticationProvider.setPasswordEncoder(myPasswordEncoder);
        return authenticationProvider;
    }

}