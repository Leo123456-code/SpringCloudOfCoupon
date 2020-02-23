package com.imooc.coupon.service;

import com.imooc.coupon.entry.CouponTemplate;

/**
 * 异步服务接口定义
 * created by Leo徐忠春
 * created Time 2020/2/20-2:55
 * email 1437665365@qq.com
 */
public interface IAsyncService {
    /**
     * <h2>根据模板异步的创建优惠券码</h2>
     * @param template {@link CouponTemplate} 优惠券模板实体
     * */
    void asyncConstructCouponByTemplate(CouponTemplate template);
}
