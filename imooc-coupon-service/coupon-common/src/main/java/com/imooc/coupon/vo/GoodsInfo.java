package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品信息
 * created by Leo徐忠春
 * created Time 2020/2/22-3:13
 * email 1437665365@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfo {
    //商品类型 -映射到Goodstype 枚举类
    private Integer type;
    //商品价格
    private double price;
    //商品数量
    private Integer count;

    ////TODO 商品名称 ,使用信息
}
