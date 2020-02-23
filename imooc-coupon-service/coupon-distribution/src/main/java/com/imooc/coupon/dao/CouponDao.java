package com.imooc.coupon.dao;

import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.entry.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  分发微服务接口定义
 * created by Leo徐忠春
 * created Time 2020/2/21-5:27
 * email 1437665365@qq.com
 */
public interface CouponDao extends JpaRepository<Coupon,Integer> {
    //根据UserId + 状态寻找优惠券记录
    //where userId=  and status=
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
