package com.imooc.coupon.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 定制 Http 消息转换器
 * created by Leo徐忠春
 * created Time 2020/2/19-0:28
 * email 1437665365@qq.com
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     * 将JAVA实体对象转换为Http的数据输出流
     * @param converters
     */
    @Override
    public void configureMessageConverters
            (List<HttpMessageConverter<?>> converters) {

        converters.clear();
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
