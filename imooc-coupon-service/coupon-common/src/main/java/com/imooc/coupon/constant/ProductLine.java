package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 *  產品線枚舉
 * created by Leo徐忠春
 * created Time 2020/2/19-23:34
 * email 1437665365@qq.com
 */
@Getter
@AllArgsConstructor
public enum ProductLine {
    DAMAO("大貓",1),
    DABAO("大寶",2),
    ;
    //產品線描述
    private String description;
    //產品線編碼
    private Integer code;

    /**
     * 根据code返回对应的枚举
     * @param code
     * @return
     */
    public static  ProductLine of(Integer code){
        //判斷code不能為空
        Objects.requireNonNull(code);
        return Stream.of(values()).filter(bean -> bean.code.equals(code)).
                findAny().orElseThrow(()-> new IllegalArgumentException(
                        code+" not exists!"
        ));

    }
}
