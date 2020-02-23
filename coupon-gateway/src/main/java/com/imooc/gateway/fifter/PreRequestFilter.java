package com.imooc.gateway.fifter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 在过滤器中存储客户端发起请求的时间戳
 * created by Leo徐忠春
 * created Time 2020/2/18-3:12
 * email 1437665365@qq.com
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class PreRequestFilter extends  AbstractPreZuulFilter{
    /**
     *
     * @return
     */
    @Override
    protected Object cRun() {

        context.set("startTime",System.currentTimeMillis());

        return success();
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
