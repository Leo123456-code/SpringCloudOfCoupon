package com.imooc.coupon.feign;

import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.feign.hystric.SettlementClientHystric;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 优惠券结算 微服务 Fegin 接口定义
 * created by Leo徐忠春
 * created Time 2020/2/22-7:32
 * email 1437665365@qq.com
 */
@FeignClient(value = "eureka-client-coupon-settlement",
fallback = SettlementClientHystric.class)
public interface SettlementClient {
    /**
     * 优惠券规则计算
     */
    @RequestMapping(value = "coupon-settlement/settlement/compute",
    method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule
    (@RequestBody SettlementInfo settlement) throws CouponException;

}
