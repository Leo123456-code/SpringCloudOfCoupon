package com.imooc.coupon.feign;

import com.imooc.coupon.feign.hystric.TemplateClientHystric;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.CouponTemplateSDK;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板微服务 Feign 接口定义
 * created by Leo徐忠春
 * created Time 2020/2/22-7:20
 * email 1437665365@qq.com
 */
//value 服务应用名
@FeignClient(value = "eureka-client-coupon-template",
        fallback = TemplateClientHystric.class)
public interface TemplateClient {
    /**
     * 查找所有可用的优惠券模板
     */
    @RequestMapping(value = "coupon-template/template/sdk/all",
    method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate();
    /**
     * 獲取模板ids 到 CouponTemplateSDK 的映射
     */
    @RequestMapping(value = "coupon-template/template/sdk/info",
    method = RequestMethod.GET)
    CommonResponse<Map<Integer,CouponTemplateSDK>> findIds2Template
    (@RequestParam("ids") Collection<Integer> ids);

}
