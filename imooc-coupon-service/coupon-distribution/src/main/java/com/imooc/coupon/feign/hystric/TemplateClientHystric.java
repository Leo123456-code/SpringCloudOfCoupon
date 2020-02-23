package com.imooc.coupon.feign.hystric;

import com.imooc.coupon.feign.TemplateClient;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板 Fegin 接口的熔断降级策略
 * created by Leo徐忠春
 * created Time 2020/2/22-7:40
 * email 1437665365@qq.com
 */
@Slf4j
@Component
public class TemplateClientHystric implements TemplateClient {
    /**
     *
     * @return
     */
    @Override
    public CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate() {
        log.error("[eureka-client-coupon-template] findAllUsableTemplate request error");
        return new CommonResponse<>(-1,"[eureka-client-coupon-template] request error",
                Collections.emptyList());
    }

    /**
     *
     * @param ids
     * @return
     */
    @Override
    public CommonResponse<Map<Integer, CouponTemplateSDK>>
    findIds2Template(Collection<Integer> ids) {
        log.error("[eureka-client-coupon-template] findIds2Template request error");
        return new CommonResponse<>(-1,"[eureka-client-coupon-template] request error",
                Collections.emptyMap());
    }
}
