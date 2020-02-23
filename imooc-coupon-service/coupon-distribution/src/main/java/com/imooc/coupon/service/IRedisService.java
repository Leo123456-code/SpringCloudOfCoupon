package com.imooc.coupon.service;

import com.imooc.coupon.entry.Coupon;
import com.imooc.coupon.exception.CouponException;

import java.util.List;

/**
 * Redis 相关的操作服务接口定义
 * 1.用户的三个状态优惠券 Cache 相关操作
 * 2.优惠券模板生成的优惠券码 Cache 操作
 *
 *
 * created by Leo徐忠春
 * created Time 2020/2/22-2:22
 * email 1437665365@qq.com
 */
public interface IRedisService {
    //根据userId 和 优惠券状态找到缓存的优惠券列表数据
    List<Coupon> getCachedCoupons(Long userId,Integer status);

    /**
     * 保存空的优惠券列表到缓存中
     * 避免缓存穿透
     * @param userId
     * @param status
     */
    void saveEmptyCouponListToCache(Long userId,List<Integer> status);
    /**
     * 尝试从Cache 中获取一个优惠券码
     */
    String tryToAcquireCouponCodeFormCache(Integer templateId);
    /**
     * 将优惠券保存到Cache 中
     * 返回一个成功的个数
     */
    Integer addCouponToCache(Long userId,List<Coupon> coupons,
                             Integer status) throws CouponException;
}
