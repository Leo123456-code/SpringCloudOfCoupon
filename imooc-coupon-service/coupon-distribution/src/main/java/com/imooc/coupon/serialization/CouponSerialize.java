package com.imooc.coupon.serialization;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.coupon.entry.Coupon;
import com.imooc.coupon.entry.CouponTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 优惠券实体类自定义序列化器
 * created by Leo徐忠春
 * created Time 2020/2/21-5:04
 * email 1437665365@qq.com
 */

public class CouponSerialize extends JsonSerializer<Coupon> {

    @Override
    public void serialize(Coupon coupon, JsonGenerator gen,
                          SerializerProvider serializers)
            throws IOException {
        //开始序列化
        gen.writeStartObject();

        gen.writeStringField("id",coupon.getId().toString());
        gen.writeStringField("templateId",
                coupon.getTemplateId().toString());
        gen.writeStringField("couponCode",coupon.getCouponCode());
        gen.writeStringField("assignTime",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(coupon.getAssignTime()));
        gen.writeStringField("status",coupon.getStatus().getCode().toString());
        gen.writeStringField("name",coupon.getTemplateSDK().getName());
        gen.writeStringField("logo",coupon.getTemplateSDK().getLogo());
        gen.writeStringField("desc",coupon.getTemplateSDK().getDesc());
        gen.writeStringField("expiration", JSON.toJSONString
                (coupon.getTemplateSDK().getRule().getExpriration()));
        gen.writeStringField("discount",JSON.toJSONString(
                coupon.getTemplateSDK().getRule().getDiscount()));
        gen.writeStringField("usage",JSON.toJSONString(
                coupon.getTemplateSDK().getRule().getUsage()));
        //结束序列化
        gen.writeEndObject();

    }
}
