package com.example.demo.mybatis.pagehelper.interceptor;

import com.example.demo.vo.PageData;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args={Statement.class})
})
@Slf4j
public class SeedPageResultInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (PageHelper.getLocalPage() != null && !isCount(invocation)) {
            if (result instanceof List) {
                PageData pageData = new PageData(PageHelper.getLocalPage().getTotal(), (List<?>) result);

                List<PageData> pageDataList = new ArrayList<>(1);
                pageDataList.add(pageData);
                return pageDataList;
            }
        }

        return result;
    }

    private boolean isCount(Invocation invocation) {
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(resultSetHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
        return mappedStatement.getId().endsWith("_COUNT");
    }

}
