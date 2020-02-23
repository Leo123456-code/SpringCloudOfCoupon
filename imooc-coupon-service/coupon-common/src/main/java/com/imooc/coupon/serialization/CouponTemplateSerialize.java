package com.imooc.coupon.serialization;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.imooc.coupon.entry.CouponTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  優惠券模板實體類自定義序列化器
 * created by Leo徐忠春
 * created Time 2020/2/20-1:35
 * email 1437665365@qq.com
 */
public class CouponTemplateSerialize extends JsonSerializer<CouponTemplate> {
    /**
     *
     * @param value
     * @param gen
     * @param serializers
     * @throws IOException
     */
    @Override
    public void serialize(CouponTemplate value, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        //開始序列化對象
        gen.writeStartObject();
        gen.writeStringField("id",value.getId().toString());
        gen.writeStringField("name",value.getName());
        gen.writeStringField("logo",value.getLogo());
        gen.writeStringField("desc",value.getDesc());
        gen.writeStringField("category",value.getCategory().getDescription());
        gen.writeStringField("productLine",value.getProductLine().getDescription());
        gen.writeStringField("count",value.getCount().toString());
        gen.writeStringField("createTime",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        gen.writeStringField("uerId",value.getUserId().toString());
        // 优惠券模板唯一编码 = 4(产品线和类型) + 8(日期: 20190101) + id(扩充为4位)
        gen.writeStringField("key",value.getKey()+
                String.format("%04d",value.getId()));
        gen.writeStringField("target",value.getTarget().getDescription());
        gen.writeStringField("rule", JSON.toJSONString(value.getRule()));
        //結束序列化對象
        gen.writeEndObject();

    }
}
