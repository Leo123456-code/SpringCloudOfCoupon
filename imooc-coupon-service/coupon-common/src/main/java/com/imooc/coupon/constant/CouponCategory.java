package com.imooc.coupon.constant;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 优惠券分类枚举
 * created by Leo徐忠春
 * created Time 2020/2/19-6:35
 * email 1437665365@qq.com
 */
@Getter
public enum  CouponCategory {
    MANJIAN("满减券","001"),
    ZHEKOU("折扣券","002"),
    LIJIAN("立减券","003"),
    ;

    CouponCategory(String description, String code) {
        this.description = description;
        this.code = code;
    }

    //优惠券分类描述信息
    private String description;
    //优惠券分类编码
    private String code;

    /**
     * 根据code返回对应的枚举
     * @param code
     * @return
     */
    public static CouponCategory of(String code){
        //判断code不能为空
        Objects.requireNonNull(code);
        return Stream.of(values()).filter(bean -> bean.code.equals(code)).
                findAny().orElseThrow(()-> new IllegalArgumentException(code+
                " not exists!"));
    }
}
