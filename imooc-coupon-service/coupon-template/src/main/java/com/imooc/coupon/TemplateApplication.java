package com.imooc.coupon;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * created by Leo徐忠春
 * created Time 2020/2/19-4:46
 * email 1437665365@qq.com
 */
@SpringBootApplication
@EnableEurekaClient
//定时任务注解
@EnableScheduling
//Jpa註解
@EnableJpaAuditing

public class TemplateApplication {

    public static void main(String[] args) {

        SpringApplication.run(TemplateApplication.class,args);
    }
}
