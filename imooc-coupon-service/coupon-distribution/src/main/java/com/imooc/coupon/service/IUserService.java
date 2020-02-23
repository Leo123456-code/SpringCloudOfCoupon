package com.imooc.coupon.service;

import com.imooc.coupon.entry.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.AcquireTemplateRequest;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;

import java.util.List;

/**
 *  用户服务相关的接口定义
 *  1.用户三类状态优惠券信息展示服务
 *  2.查看用户当前可以领取的优惠券模板
 *  3.用户领取优惠券服务
 *  4.用户消费优惠券服务 --coupon-settlement  微服务配合实现
 *
 * created by Leo徐忠春
 * created Time 2020/2/22-2:41
 * email 1437665365@qq.com
 */
public interface IUserService {
    /**
     * 根据用户id 和状态查询优惠券记录
     */
    List<Coupon> findCouponsByStatus(Long userId,Integer status) throws CouponException;
    /**
     * 根据用户id查找当前可以领取的优惠券模板
     */
    List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException;
    /**
     * 用户领取优惠券
     */
    Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException;
    /**
     *结算(核销)优惠券
     * return SettlementInfo;
     */
    SettlementInfo settlent(SettlementInfo info) throws CouponException;
}
