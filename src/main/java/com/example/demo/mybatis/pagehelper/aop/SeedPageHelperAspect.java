package com.example.demo.mybatis.pagehelper.aop;

import com.example.demo.mybatis.pagehelper.annotation.SeedPage;
import com.example.demo.qo.BasePageQo;
import com.github.pagehelper.PageHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;


@Aspect
public class SeedPageHelperAspect {

    @Before(value = "@annotation(seedPage)")
    public void startPage(JoinPoint joinPoint, SeedPage seedPage) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length >= 1) {
            for (Object arg : args) {
                if (arg instanceof BasePageQo) {
                    BasePageQo pageQo = (BasePageQo) arg;
                    PageHelper.startPage(pageQo.getPageNum(), pageQo.getPageSize());
                    break;
                }
            }
        }
    }
}
