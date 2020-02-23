package com.imooc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
//网关注解
@EnableZuulProxy
@SpringCloudApplication
public class CouponGatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(CouponGatewayApplication.class, args);
    }

}
