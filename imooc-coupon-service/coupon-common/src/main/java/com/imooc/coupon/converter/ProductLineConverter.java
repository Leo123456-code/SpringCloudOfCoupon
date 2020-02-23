package com.imooc.coupon.converter;

import com.imooc.coupon.constant.ProductLine;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 產品線分類枚舉 屬性轉換器
 * created by Leo徐忠春
 * created Time 2020/2/20-1:09
 * email 1437665365@qq.com
 */
@Converter
public class ProductLineConverter implements
        AttributeConverter<ProductLine,Integer> {
    /**
     * 將實體屬性X轉換為Y存儲到數據庫中,插入和更新時執行的動作
     * @param attribute
     * @return
     */
    @Override
    public Integer convertToDatabaseColumn(ProductLine attribute) {
        return attribute.getCode();
    }

    /**
     * 將數據庫中的字段Y轉換為實體屬性X,查詢操作時執行的動作
     * @param
     * @return
     */
    @Override
    public ProductLine convertToEntityAttribute(Integer code) {
        return ProductLine.of(code);
    }
}
