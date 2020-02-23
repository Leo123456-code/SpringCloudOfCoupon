package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微服務之間用的優惠券模板信息定義
 * created by Leo徐忠春
 * created Time 2020/2/20-2:19
 * email 1437665365@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponTemplateSDK {
    //優惠券模板主鍵
    private Integer id;
    //優惠券名稱
    private String name;
    //優惠券logo
    private String logo;
    //優惠券描述信息
    private String desc;
    //優惠券分類
    private String category;
    //優惠券產品線
    private Integer productLine;
    //優惠券模板的編碼
    private String key;
    //優惠券目標用戶
    private Integer target;
    //優惠券規則
    private TemplateRule rule;

}
