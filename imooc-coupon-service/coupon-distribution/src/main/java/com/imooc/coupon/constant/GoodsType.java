package com.imooc.coupon.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 商品类型枚举
 * created by Leo徐忠春
 * created Time 2020/2/22-3:00
 * email 1437665365@qq.com
 */
@Getter
@AllArgsConstructor
public enum GoodsType {
    WENYU("文娱",1),
    SHENGXIAN("生鲜",2),
    JIAJU("家居",3),
    OTHERS("其他",4),
    ALL("全品类",5),
    ;
    //描述
    private String description;
    //商品类型编码
    private Integer code;

    /**
     * 根据code 获取到
     * @param code
     * @return
     */
    public static GoodsType of(Integer code){
        Objects.requireNonNull(code);

        return Stream.of(values()).filter(bean->bean.code.equals(code))
                .findAny().orElseThrow(()->new IllegalArgumentException(
                        code+" not exists! "
                ));
    }
}
