package com.imooc.gateway.fifter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * created by Leo徐忠春
 * created Time 2020/2/18-3:20
 * email 1437665365@qq.com
 */
@Slf4j
@Component
public class AccessLogFilter extends AbstractPostZuulFilter{
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        //从PreRequestFilter 中 获取设置的请求时间戳
        Long startTime = (Long) context.get("startTime");
        String uri = request.getRequestURI();
        long endTime = System.currentTimeMillis()-startTime;
        //从网关通过的请求都会打印日志记录
        log.info("uri={},endTime={}",uri,endTime);

        return success();
    }

    @Override
    public int filterOrder() {

        return FilterConstants.SEND_RESPONSE_FILTER_ORDER-1;
    }
}
