package com.imooc.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 优惠券结算微服务的启动入口
 * created by Leo徐忠春
 * created Time 2020/2/23-22:45
 * email 1437665365@qq.com
 */
@EnableEurekaClient
@SpringBootApplication
public class SettlementApplication {

    public static void main(String[] args) {

        SpringApplication.run(SettlementApplication.class,args);
    }
}
