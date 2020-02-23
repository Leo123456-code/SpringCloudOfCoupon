package com.imooc.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * kafka 相关的服务接口定义
 * created by Leo徐忠春
 * created Time 2020/2/22-2:37
 * email 1437665365@qq.com
 */
public interface IkafaService {
    /**
     * 消费优惠券 kafka 消息
     * @param record
     */
    void consumerCouponKafkaMessage(ConsumerRecord<?,?> record);
}
