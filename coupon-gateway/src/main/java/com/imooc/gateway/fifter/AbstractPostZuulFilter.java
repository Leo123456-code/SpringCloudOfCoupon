package com.imooc.gateway.fifter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * created by Leo徐忠春
 * created Time 2020/2/18-2:32
 * email 1437665365@qq.com
 */
@Component
public abstract class AbstractPostZuulFilter extends AbstractZuulFifter{
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
}
