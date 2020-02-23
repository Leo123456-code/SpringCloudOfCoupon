package com.imooc.coupon.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * Jackson 的自定义配置
 * created by Leo徐忠春
 * created Time 2020/2/19-0:38
 * email 1437665365@qq.com
 */
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        //设置时间格式
        mapper.setDateFormat(new SimpleDateFormat
                ("yyyy-MM-dd HH:mm:ss"));
        //json中值为null不返回
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
