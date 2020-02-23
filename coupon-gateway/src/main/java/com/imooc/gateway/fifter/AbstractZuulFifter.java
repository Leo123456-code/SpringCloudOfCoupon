package com.imooc.gateway.fifter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.RequestContent;
import org.springframework.stereotype.Component;

/**
 *  通用的抽象过滤器类
 * created by Leo徐忠春
 * created Time 2020/2/18-1:42
 * email 1437665365@qq.com
 */
@Component
@Slf4j
public abstract class AbstractZuulFifter extends ZuulFilter {
    //用于在过滤器之间传递消息,数据保存在每个请求的 ThreadLocal 中,拓展了 Map
    RequestContext context;
    private final static String NEXT = "next";//下一个过滤器执行的意思

    /**
     * 从当前线程中获取RequestContext
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        log.info("This is AbstractZuulFifter");
        return (boolean)ctx.getOrDefault(NEXT,true);
    }

    @Override
    public Object run() throws ZuulException {
        context = RequestContext.getCurrentContext();

        return cRun();
    }

    protected abstract Object cRun();
    //失败返回
    Object fail(int code,String msg){
        context.set(NEXT,false);
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
        context.setResponseBody(String.format("{\"result\": \"%s!\"}",msg));
        return null;
    }
    //成功返回
    Object success(){
        context.set(NEXT,true);
        return null;
    }
}
