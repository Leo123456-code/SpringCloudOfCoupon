package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 分發目標 的枚舉
 * created by Leo徐忠春
 * created Time 2020/2/19-23:43
 * email 1437665365@qq.com
 */
@Getter
@AllArgsConstructor
public enum DistributeTarget {
    SINGLE("單用戶",1),
    MULTI("多用戶",2),
    ;
    //分發目標描述
    private String description;
    //分發目標編碼
    private Integer code;

    /**
     * 根据code返回对应的枚举
     * @param code
     * @return
     */
    public static DistributeTarget of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values()).filter(bean ->bean.code.equals(code))
                .findAny().orElseThrow(()->
                        new IllegalArgumentException(code+ " not exists!"));
    }
}
