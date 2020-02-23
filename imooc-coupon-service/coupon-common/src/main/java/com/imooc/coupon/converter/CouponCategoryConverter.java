package com.imooc.coupon.converter;


import com.imooc.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 優惠券分類枚舉 屬性轉換器
 * AttributeConverter<X,Y>
 *     X是實體屬性的類型
 *     Y是數據庫字段的類型
 * created by Leo徐忠春
 * created Time 2020/2/20-0:38
 * email 1437665365@qq.com
 */
//標識這是一個轉換器
@Converter
public class CouponCategoryConverter implements
        AttributeConverter<CouponCategory,String> {
    /**
     *  將實體屬性X轉換為Y存儲到數據庫中,插入和更新時執行的動作
     * @param attribute
     * @return
     */
    @Override
    public String convertToDatabaseColumn(CouponCategory attribute) {
        return attribute.getCode();
    }

    /**
     *  將數據庫中的字段Y轉換為實體屬性X,查詢操作時執行的動作
     * @param
     * @return
     */
    @Override
    public CouponCategory convertToEntityAttribute(String code) {

        return CouponCategory.of(code);
    }
}
