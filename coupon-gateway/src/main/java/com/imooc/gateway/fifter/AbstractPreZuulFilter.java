package com.imooc.gateway.fifter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * created by Leo徐忠春
 * created Time 2020/2/18-2:28
 * email 1437665365@qq.com
 */
@Component
public abstract class AbstractPreZuulFilter extends AbstractZuulFifter {
    /**
     *
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
}
