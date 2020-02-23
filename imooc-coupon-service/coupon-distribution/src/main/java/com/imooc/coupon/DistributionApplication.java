package com.imooc.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

/**
 *  分发微服务的入口
 * created by Leo徐忠春
 * created Time 2020/2/21-3:56
 * email 1437665365@qq.com
 */
//@SpringCloudApplication
@EnableJpaAuditing
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
//hystrix 服务熔断降级
@EnableCircuitBreaker
public class DistributionApplication {
    /**
     * 定义 RestTemplate
     * @LoadBalanced 实现负载均衡
     * @return
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {

        SpringApplication.run(DistributionApplication.class,args);
    }

}
