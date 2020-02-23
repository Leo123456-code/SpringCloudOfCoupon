package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 优惠券 Kafka 消息对象定义
 * created by Leo徐忠春
 * created Time 2020/2/22-6:41
 * email 1437665365@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponKafkaMessage {
    //优惠券状态
    private Integer status;
    //Coupon 主键
    private List<Integer> ids;
}
