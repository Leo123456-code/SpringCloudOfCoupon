package com.imooc.coupon.converter;

import com.imooc.coupon.constant.DistributeTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 分發目標枚舉 屬性轉換器
 * created by Leo徐忠春
 * created Time 2020/2/20-1:13
 * email 1437665365@qq.com
 */
@Converter
public class DistributeTargetConverter implements
        AttributeConverter<DistributeTarget,Integer> {
    /**
     * 將實體屬性X轉換為Y存儲到數據庫中,插入和更新時執行的動作
     * @param attribute
     * @return
     */
    @Override
    public Integer convertToDatabaseColumn(DistributeTarget attribute) {

        return attribute.getCode();
    }

    /**
     * 將數據庫中的字段Y轉換為實體屬性X,查詢操作時執行的動作
     * @param code
     * @return
     */
    @Override
    public DistributeTarget convertToEntityAttribute(Integer code) {

        return DistributeTarget.of(code);
    }
}
