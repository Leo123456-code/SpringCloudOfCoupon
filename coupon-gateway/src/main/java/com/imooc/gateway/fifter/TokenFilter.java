package com.imooc.gateway.fifter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 *  效验请求中传递的Token
 * created by Leo徐忠春
 * created Time 2020/2/18-2:34
 * email 1437665365@qq.com
 */
@Component
@Slf4j
public class TokenFilter extends AbstractPreZuulFilter{
    /**
     * context在 AbstractZuulFifter已完成初始化了
     * @return
     */
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s request to %s",request.getMethod(),
                request.getRequestURL().toString()));
        Object token = request.getParameter("token");
        if(null==token){
            log.error("error : token is empty");
            return fail(401,"token is empty");
        }

        return success();
    }

    /**
     * 执行顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }
}
