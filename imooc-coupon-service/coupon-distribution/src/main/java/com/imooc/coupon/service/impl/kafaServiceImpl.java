package com.imooc.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.dao.CouponDao;
import com.imooc.coupon.entry.Coupon;
import com.imooc.coupon.service.IkafaService;
import com.imooc.coupon.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Kafka 相关的服务接口实现
 * 核心思想: 是将Cache 中的Coupon的状态变化同步到 DB中
 * created by Leo徐忠春
 * created Time 2020/2/22-6:31
 * email 1437665365@qq.com
 */
@Component
@Slf4j
public class kafaServiceImpl implements IkafaService {
    @Autowired
    private CouponDao couponDao;


    /**
     * //TODO Kafka
     * 消费优惠券 kafka 消息
     * @param record
     */
    @Override
    //监听
    @KafkaListener(topics = {Constant.TOPIC},groupId = "imooc-coupon-1")
    public void consumerCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        //获取kafka消息
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        //如果消息存在
        if(kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            //把 CouponKafkaMessage 转换成 Object类型
            CouponKafkaMessage couponInfo =
                    JSON.parseObject(message.toString(), CouponKafkaMessage.class);
            log.info("Reseive CouponKafkaMessage: {}",message.toString());
            //获取优惠券状态
            CouponStatus status = CouponStatus.of(couponInfo.getStatus());
            switch (status){
                case USABLE:
                    break;
                case USED:
                    break;
                case EXPIRED:
                    break;
            }
        }

    }
    /**
     * 处理已使用的优惠券
     */
    private void processUsedCoupons(CouponKafkaMessage kafkaMessage,
                                    CouponStatus status){
        //TODO 给用户发送消息
        processCouponsByStatus(kafkaMessage,status);
    }
    /**
     * 处理已过期的优惠券
     */
    private void processExpiredCoupons(CouponKafkaMessage kafkaMessage,
                                       CouponStatus status){
        //TODO 给用户消息推送
        processCouponsByStatus(kafkaMessage,status);
    }
    /**
     * 根据状态处理优惠券信息
     */
    private void processCouponsByStatus(CouponKafkaMessage kafkaMessage,
                                        CouponStatus status){
        List<Coupon> coupons = couponDao.findAllById(kafkaMessage.getIds());
        if(CollectionUtils.isEmpty(coupons)|| coupons.size() !=kafkaMessage.getIds().size()){
            log.error("Can Not Find Right Coupon Info: {}",JSON.toJSONString(kafkaMessage));
            //TODO 发送邮件
            return;
        }

        coupons.forEach(c->c.setStatus(status));
        log.info("CouponKafkaMessage Op Coupon Count: {}",couponDao.saveAll(coupons).size());
    }
}
