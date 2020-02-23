package com.imooc.gateway.fifter;


import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 *  限流过滤器
 *  限制多少流量和人数进入网站,基于令牌桶进行流量限制
 * created by Leo徐忠春
 * created Time 2020/2/18-2:50
 * email 1437665365@qq.com
 */
@Slf4j
@Component
//忽略代码中所有的警告信息
@SuppressWarnings("all")
public class RateLimiterFilter extends AbstractPreZuulFilter{
    //取令牌,每秒可以获取到两个令牌
    RateLimiter rateLimiter = RateLimiter.create(2.0);


    /**
     *
     * @return
     */
    @Override
    protected Object cRun() {
        //获取当前的请求对象
        HttpServletRequest request = context.getRequest();
        //尝试去获取令牌
        if(rateLimiter.tryAcquire()){
            return success();
        }else {
            log.error("限流,限流地址IP={}",request.getRequestURL());
            return fail(402,"限流中");
        }

    }

    /**
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 2;
    }
}
