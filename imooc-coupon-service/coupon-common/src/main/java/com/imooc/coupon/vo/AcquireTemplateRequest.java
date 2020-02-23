package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取优惠券请求对象定义
 * created by Leo徐忠春
 * created Time 2020/2/22-2:51
 * email 1437665365@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcquireTemplateRequest {
    //用户id
    private Long userId;
    //优惠券模板信息
    private CouponTemplateSDK templateSDK;
}
