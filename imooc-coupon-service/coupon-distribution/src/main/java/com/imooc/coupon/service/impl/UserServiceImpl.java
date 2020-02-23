package com.imooc.coupon.service.impl;


import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.dao.CouponDao;
import com.imooc.coupon.entry.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.feign.SettlementClient;
import com.imooc.coupon.feign.TemplateClient;
import com.imooc.coupon.service.IRedisService;
import com.imooc.coupon.service.IUserService;
import com.imooc.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户服务相关的接口实现
 * 所有的操作过程,状态都保存在 Redis中,并通过 Kafka 把消息传递到mysql中
 * 为什么使用Kafka,而不是直接使用 SpringBoot  异步处理
 * 1.安全
 *
 *
 * created by Leo徐忠春
 * created Time 2020/2/22-8:21
 * email 1437665365@qq.com
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private IRedisService redisService;
    //模板微服务客户端
    @Autowired
    private TemplateClient templateClient;
    //结算微服务客户端
    @Autowired
    private SettlementClient settlementClient;
    //kafka客户端
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    /**
     *根据用户id 和状态查询优惠券记录
     * @param userId
     * @param status
     * @return
     */
    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException {
        //查询redis,从缓存中获取优惠券
        List<Coupon> curCache = redisService.getCachedCoupons(userId, status);

        List<Coupon> preTarget;
        if(CollectionUtils.isNotEmpty(curCache)){
            log.debug("coupon cache is not empty: {},{}",userId,status);
            preTarget = curCache;
        }else {
            log.debug("coupon cache is empty,get coupon from db: {},{}",userId,status);
            List<Coupon> dbCoupons =
                    couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            //如果数据库中没有记录,直接返回就可以了,Cache中 已经加入了一张无效的优惠券
            if(CollectionUtils.isEmpty(dbCoupons)){
                log.debug("current user do not have coupon: {},{}",userId,status);
                return dbCoupons;
            }
            //填充dbCoupons 的templateSDK 字段
            Map<Integer, CouponTemplateSDK> id2TemplateSDK = templateClient.findIds2Template(dbCoupons.stream().
                    map(Coupon::getTemplateId).collect(Collectors.toList())).getData();
            //dc代表每一张优惠券
            dbCoupons.forEach(dc->dc.setTemplateSDK(id2TemplateSDK.get(dc.getTemplateId())));
            //数据库存在记录
            preTarget = dbCoupons;
            //将记录写入 Cache
            redisService.addCouponToCache(userId,preTarget,status);
        }
        //将无效的优惠券剔除
        preTarget = preTarget.stream().filter(c->c.getId() !=-1).collect(Collectors.toList());
        //如果当前获取的是可用优惠券,还需要对已过期的优惠券做延迟处理
        if(CouponStatus.of(status) == CouponStatus.USABLE){
            CouponClassify classify = CouponClassify.classify(preTarget);
            //如果已过期状态不为空,需要延迟处理
            if(CollectionUtils.isNotEmpty(classify.getExpired())){
               log.info("Add Expired Coupons To Cache Form FindCouponsByStatus: {},{}",userId,status);
               redisService.addCouponToCache(userId,classify.getExpired(),CouponStatus.EXPIRED.getCode());
               //将过期策略发送到kafka中 做异步处理
                kafkaTemplate.send(Constant.TOPIC, JSON.toJSONString(new CouponKafkaMessage(
                        CouponStatus.EXPIRED.getCode(),classify.getExpired().stream().map(
                                Coupon::getId).collect(Collectors.toList()))));
            }
            return classify.getUsable();
        }
        return preTarget;
    }

    /**
     *根据用户id查找当前可以领取的优惠券模板
     * @param userId
     * @return
     */
    @Override
    public List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException {
        Long curTime = new Date().getTime();
        List<CouponTemplateSDK> templateSDKS = templateClient.findAllUsableTemplate().getData();
        log.debug(" Find All Template Count:{}",templateSDKS.size());
        //过滤过期的优惠券模板
        templateSDKS.stream().filter(t->t.getRule().getExpriration().
                getDeadline() > curTime).collect(Collectors.toList());
        log.info("Find Usable Template Count: {}",templateSDKS.size());
        //key 是 TemplateId
        //value 中 的left 是 Template limitation ,right 是优惠券模板
        Map<Integer, Pair<Integer, CouponTemplateSDK>> limit2Template =
                new HashMap<>(templateSDKS.size());
        templateSDKS.forEach(
                t -> limit2Template.put(
                        t.getId(),
                        Pair.of(t.getRule().getLimitation(), t)
                )
        );

        List<CouponTemplateSDK> result =
                new ArrayList<>(limit2Template.size());
        List<Coupon> userUsableCoupons = findCouponsByStatus(
                userId, CouponStatus.USABLE.getCode()
        );

        log.debug("Current User Has Usable Coupons: {}, {}", userId,
                userUsableCoupons.size());

        // key 是 TemplateId
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons
                .stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));

        // 根据 Template 的 Rule 判断是否可以领取优惠券模板
        limit2Template.forEach((k, v) -> {

            int limitation = v.getLeft();
            CouponTemplateSDK templateSDK = v.getRight();

            if (templateId2Coupons.containsKey(k)
                    && templateId2Coupons.get(k).size() >= limitation) {
                return;
            }

            result.add(templateSDK);

        });

        return result;
    }

    /**
     *用户领取优惠券
     * 1.从Template 中拿到对应的优惠券,并检查是否过期
     * 2.根据limitation 判断用户是否可以领取
     *
     * @param request
     * @return
     */
    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException {
        Map<Integer, CouponTemplateSDK> id2Template =
                templateClient.findIds2Template(
                        Collections.singletonList(
                                request.getTemplateSDK().getId()
                        )
                ).getData();

        // 优惠券模板是需要存在的
        if (id2Template.size() <= 0) {
            log.error("Can Not Acquire Template From TemplateClient: {}",
                    request.getTemplateSDK().getId());
            throw new CouponException("Can Not Acquire Template From TemplateClient");
        }

        // 用户是否可以领取这张优惠券
        List<Coupon> userUsableCoupons = findCouponsByStatus(
                request.getUserId(), CouponStatus.USABLE.getCode()
        );
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons
                .stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));

        if (templateId2Coupons.containsKey(request.getTemplateSDK().getId())
                && templateId2Coupons.get(request.getTemplateSDK().getId()).size() >=
                request.getTemplateSDK().getRule().getLimitation()) {
            log.error("Exceed Template Assign Limitation: {}",
                    request.getTemplateSDK().getId());
            throw new CouponException("Exceed Template Assign Limitation");
        }

        // 尝试去获取优惠券码
        String couponCode = redisService.tryToAcquireCouponCodeFormCache(
                request.getTemplateSDK().getId()
        );
        if (StringUtils.isEmpty(couponCode)) {
            log.error("Can Not Acquire Coupon Code: {}",
                    request.getTemplateSDK().getId());
            throw new CouponException("Can Not Acquire Coupon Code");
        }

        Coupon newCoupon = new Coupon(
                request.getTemplateSDK().getId(), request.getUserId(),
                couponCode, CouponStatus.USABLE
        );
        newCoupon = couponDao.save(newCoupon);

        // 填充 Coupon 对象的 CouponTemplateSDK, 一定要在放入缓存之前去填充
        newCoupon.setTemplateSDK(request.getTemplateSDK());

        // 放入缓存中
        redisService.addCouponToCache(
                request.getUserId(),
                Collections.singletonList(newCoupon),
                CouponStatus.USABLE.getCode()
        );

        return newCoupon;
    }

    /**
     *结算(核销)优惠券
     * @param info
     * @return
     */
    @Override
    public SettlementInfo settlent(SettlementInfo info) throws CouponException {
        // 当没有传递优惠券时, 直接返回商品总价
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos =
                info.getCouponAndTemplateInfos();
        if (CollectionUtils.isEmpty(ctInfos)) {

            log.info("Empty Coupons For Settle.");
            double goodsSum = 0.0;

            for (GoodsInfo gi : info.getGoodsInfos()) {
                goodsSum += gi.getPrice() + gi.getCount();
            }

            // 没有优惠券也就不存在优惠券的核销, SettlementInfo 其他的字段不需要修改
            info.setCost(retain2Decimals(goodsSum));
        }

        // 校验传递的优惠券是否是用户自己的
        List<Coupon> coupons = findCouponsByStatus(
                info.getUserId(), CouponStatus.USABLE.getCode()
        );
        Map<Integer, Coupon> id2Coupon = coupons.stream()
                .collect(Collectors.toMap(
                        Coupon::getId,
                        Function.identity()
                ));
        if (MapUtils.isEmpty(id2Coupon) || !CollectionUtils.isSubCollection(
                ctInfos.stream().map(SettlementInfo.CouponAndTemplateInfo::getId)
                        .collect(Collectors.toList()), id2Coupon.keySet()
        )) {
            log.info("{}", id2Coupon.keySet());
            log.info("{}", ctInfos.stream()
                    .map(SettlementInfo.CouponAndTemplateInfo::getId)
                    .collect(Collectors.toList()));
            log.error("User Coupon Has Some Problem, It Is Not SubCollection" +
                    "Of Coupons!");
            throw new CouponException("User Coupon Has Some Problem, " +
                    "It Is Not SubCollection Of Coupons!");
        }

        log.debug("Current Settlement Coupons Is User's: {}", ctInfos.size());

        List<Coupon> settleCoupons = new ArrayList<>(ctInfos.size());
        ctInfos.forEach(ci -> settleCoupons.add(id2Coupon.get(ci.getId())));

        // 通过结算服务获取结算信息
        SettlementInfo processedInfo =
                settlementClient.computeRule(info).getData();
        if (processedInfo.getEmploy() && CollectionUtils.isNotEmpty(
                processedInfo.getCouponAndTemplateInfos()
        )) {
            log.info("Settle User Coupon: {}, {}", info.getUserId(),
                    JSON.toJSONString(settleCoupons));
            // 更新缓存
            redisService.addCouponToCache(
                    info.getUserId(),
                    settleCoupons,
                    CouponStatus.USED.getCode()
            );
            // 更新 db
            kafkaTemplate.send(
                    Constant.TOPIC,
                    JSON.toJSONString(new CouponKafkaMessage(
                            CouponStatus.USED.getCode(),
                            settleCoupons.stream().map(Coupon::getId)
                                    .collect(Collectors.toList())
                    ))
            );
        }

        return processedInfo;
    }

    /**
     * <h2>保留两位小数</h2>
     * @param goodsSum
     * @return
     */
    private double retain2Decimals(double goodsSum) {
        // BigDecimal.ROUND_HALF_UP 代表四舍五入
        return new BigDecimal(goodsSum)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

}
