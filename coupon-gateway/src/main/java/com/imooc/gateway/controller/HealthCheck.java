package com.imooc.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康检查接口
 * created by Leo徐忠春
 * created Time 2020/2/20-6:01
 * email 1437665365@qq.com
 */
@Slf4j
@RestController
public class HealthCheck {
    @Autowired
    /*服务发现客户端*/
    private DiscoveryClient discoveryClient;
    @Autowired
    /*服务注册接口,提供了获取服务id的方法*/
    private Registration registration;
    /**
     * localhost:9000/coupon-gateway/health
     * @return
     */
    @GetMapping("/health")
    public String health(){
        log.debug("View health api...");
        return "CouponTemplate Is OK";
    }


    /**
     * 获取Eureka Server上的微服务元信息
     * @return
     */
    @GetMapping("/info")
    public List<Map<String,Object>> info(){
        //大概需要等待两分钟时间以上才能获取到注册信息
        List<Map<String,Object>> result =
                new ArrayList<>(8);
        //获取所有服务名
        List<String> serviceIds = discoveryClient.getServices();
        //遍历所有服务,获取每个服务的所有实例
        for (String serviceId : serviceIds) {
            //根据服务的id获取该服务的所有实例
            List<ServiceInstance> instances =
                    discoveryClient.getInstances(serviceId);
            for (ServiceInstance i : instances) {
                Map<String, Object> info = new HashMap<>();
                info.put("serverId",i.getServiceId());
                info.put("instanceId",i.getInstanceId());
                info.put("port",i.getPort());
                result.add(info);
                log.info("info={}",info);
            }
        }
        log.info("result={}",result);
        return result;
    }

    /**
     * 获取Eureka Server上的微服务元信息
     * 获取微服务每个服务的实例信息
     * 适用于 服务集群化
     * @return
     */
    @GetMapping("/infoToMy")
    public List<Map<String,Object>> infoToMy(){
        //根据服务id获取服务实例信息
        List<ServiceInstance> instances =
                discoveryClient.getInstances(registration.getServiceId());
        List<Map<String,Object>> results = new ArrayList<>();
        //遍历所有实例信息，把实例放入集合中
        for (ServiceInstance instance : instances) {
            Map<String, Object> map = new HashMap<>();
            map.put("instanceId",instance.getInstanceId());
            map.put("port",instance.getPort());
            map.put("serviceId",instance.getServiceId());

            results.add(map);
        }
        return results;
    }
}
