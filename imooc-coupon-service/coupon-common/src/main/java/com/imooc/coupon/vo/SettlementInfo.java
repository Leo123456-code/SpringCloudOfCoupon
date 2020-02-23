package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算信息对象定义
 * 包含：
 * 1.userId
 * 2.商品信息
 * 3.优惠券列表
 * 4.是否结算生效,即核销
 * 5.结算结果金额
 * created by Leo徐忠春
 * created Time 2020/2/22-3:17
 * email 1437665365@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {
    //用户id
    private Long userId;
    //商品信息
    private List<GoodsInfo> goodsInfos;
    //优惠券列表
    private List<CouponAndTemplateInfo> couponAndTemplateInfos;
    //是否结算生效,即核销
    private Boolean employ;
    //结果结算金额
    private double cost;

    /**
     * 优惠券列表类
     * 优惠券和模板信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponAndTemplateInfo{
        //Coupon 主键
        private Integer id;
        //优惠券对应的模板对象
        private CouponTemplateSDK templateSDK;

    }

}
