package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 用户优惠券的状态
 * created by Leo徐忠春
 * created Time 2020/2/21-4:28
 * email 1437665365@qq.com
 */
@Getter
@AllArgsConstructor
public enum CouponStatus {

    USABLE("可用的",1),
    USED("已使用的",2),
    EXPIRED("过期的(未使用的)",3);
    ;
    //优惠券状态描述信息
    private String description;
    //优惠券状态编码
    private Integer code;

    /**
     * 根据code 获取到CouponStatus
     */
    public static CouponStatus of(Integer code){
        Objects.requireNonNull(code);

        return Stream.of(values()).filter(bean-> bean.code.equals(code))
                .findAny().orElseThrow(()->new IllegalArgumentException(code
                +" not exists"));
    }
}
