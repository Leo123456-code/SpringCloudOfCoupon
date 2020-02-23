package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 有效期類型枚舉
 * created by Leo徐忠春
 * created Time 2020/2/19-23:52
 * email 1437665365@qq.com
 */
@Getter
@AllArgsConstructor
public enum PeriodType {
    REGULAR("固定的(固定日期)",1),
    SHIFT("變動的(已領取之日開始計算)",2),
    ;
    //有效期描述
    private String descirption;
    //有效期編碼
    private Integer code;

    /**
     * 根据code返回对应的枚举
     * @param code
     * @return
     */
    public static PeriodType of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values()).filter(bean -> bean.code.equals(code))
                .findAny().orElseThrow(()-> new IllegalArgumentException(
                        code+" not exists!"
                ));
    }
}
