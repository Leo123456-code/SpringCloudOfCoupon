package com.imooc.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.vo.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 優惠券規則屬性轉換器
 * created by Leo徐忠春
 * created Time 2020/2/20-1:24
 * email 1437665365@qq.com
 */
@Converter
public class RuleConverter implements
        AttributeConverter<TemplateRule,String> {
    /**
     * 將實體屬性X轉換為Y存儲到數據庫中,插入和更新時執行的動作
     * @param attribute
     * @return
     */
    @Override
    public String convertToDatabaseColumn(TemplateRule attribute) {
        return JSON.toJSONString(attribute);
    }

    /**
     * 將數據庫中的字段Y轉換為實體屬性X,查詢操作時執行的動作
     * @param rule
     * @return
     */
    @Override
    public TemplateRule convertToEntityAttribute(String rule) {
        return JSON.parseObject(rule,TemplateRule.class);
    }
}
