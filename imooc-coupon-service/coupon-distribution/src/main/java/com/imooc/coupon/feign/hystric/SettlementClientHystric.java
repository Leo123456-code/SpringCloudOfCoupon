package com.imooc.coupon.feign.hystric;

import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.feign.SettlementClient;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;

/**
 * 结算微服务 熔断策略实现
 * created by Leo徐忠春
 * created Time 2020/2/22-7:52
 * email 1437665365@qq.com
 */
@Slf4j
@Component
public class SettlementClientHystric implements SettlementClient {
    /**
     *
     * @param settlement
     * @return
     * @throws CouponException
     */
    @Override
    public CommonResponse<SettlementInfo> computeRule
    (SettlementInfo settlement) throws CouponException {
        log.error("[eureka-client-coupon-settlement] computeRule request error");
        //服务失败,不能核销
        settlement.setEmploy(false);
        settlement.setCost(1D);
        return new CommonResponse<>(-1,
                "[eureka-client-coupon-template] request error",
                settlement);
    }
}
