package com.imooc.coupon.service.impl;


import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.entry.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis相关的操作服务相关实现
 * created by Leo徐忠春
 * created Time 2020/2/22-3:37
 * email 1437665365@qq.com
 */
@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * //根据userId 和 优惠券状态找到缓存的优惠券列表数据
     * @param userId
     * @param status
     * @return
     */
    @Override
    public List<Coupon> getCachedCoupons(Long userId, Integer status) {
        log.info("Get Coupons From Cache {}, {}", userId, status);
        String redisKey = status2RedisKey(status, userId);
        List<String> coupons = redisTemplate.opsForHash().values(redisKey).stream().
                map(o -> Objects.toString(o, null)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(coupons)){
            saveEmptyCouponListToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }

        return coupons.stream().map(cs-> JSON.parseObject(cs,Coupon.class)).
                collect(Collectors.toList());
    }

    /**
     * 保存空的优惠券列表到缓存中
     * 目的: 避免缓存穿透
     * @param userId
     * @param status
     */
    @Override
    @SuppressWarnings("all")
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {
        log.info("Save Empty List To Cache For User :{}, Status: {}",
                userId, JSON.toJSONString(status));
        //key 是coupon_id, value 是序列化的Coupon
        Map<String,String> inValidCouponMap = new HashMap<>();
        //往map中存入了一个无效的优惠券信息
        inValidCouponMap.put("-1",JSON.toJSONString(Coupon.invalidCoupon()));
        /**
         * 用户优惠券缓存信息
         * K:status ->redisKey
         * v:{coupon_id:序列化的coupon}
         */
        //使用sessionCallback 把数据命令放入到redis 的pipeline,pipeline 可以一次性放入多条命令
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                status.forEach(s->{
                            String redisKey=status2RedisKey(s,userId);
                            operations.opsForHash().putAll(redisKey,inValidCouponMap);
                        }
                        );
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",
                JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));

    }

    /**
     * 尝试从Cache 中获取一个优惠券码
     * @param templateId
     * @return
     */
    @Override
    public String tryToAcquireCouponCodeFormCache(Integer templateId) {
        String redisKey=String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE,templateId);
        //因为优惠券不存在顺序关系,左边pop或右边pop，没有影响
        String couponCode = redisTemplate.opsForList().leftPop(redisKey);
        log.info("Acquire Coupon Code :{},{},{}",templateId,redisKey,couponCode);
        return couponCode;
    }

    /**
     *将优惠券保存到Cache 中
     * @param userId
     * @param coupons
     * @param status
     * @return
     */
    @Override
    public Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException {
        log.info("Add Coupon To Cache :{},{},{}",
                userId,JSON.toJSONString(coupons),status);
        Integer result = -1;
        CouponStatus couponStatus = CouponStatus.of(status);

        switch (couponStatus){
            case USABLE:
                result = addCouponToCacheForUsable(userId,coupons);
                break;
            case USED:
                result = addCouponToCacheForUsed(userId,coupons);
                break;
            case EXPIRED:
                result = addCouponToCacheForExpired(userId,coupons);
                break;
        }

        return result;
    }

    /**
     * 将已过期的优惠券放入Cache中
     * @param userId
     * @param coupons
     * @return
     */
    private Integer addCouponToCacheForExpired(Long userId, List<Coupon> coupons) throws CouponException {
        //如果status是 Expired ,代表是已有的优惠券过期了,
        //会影响2个Cache,USABLE 和 EXpired
        log.debug("Add Coupon To Cache For Expired");
        //最终需要保存的Cache
        Map<String,String> needCachedExpired = new HashMap<>();
        String redisKeyForUsable = status2RedisKey(CouponStatus.USABLE.getCode(), userId);
        String redisKeyForExpired = status2RedisKey(CouponStatus.EXPIRED.getCode(), userId);
        //获取可用的优惠券信息
        List<Coupon> curUsableCoupons = getCachedCoupons(userId, CouponStatus.USABLE.getCode());
        //获取过期的优惠券信息
        List<Coupon> curExpriedCoupons = getCachedCoupons(userId, CouponStatus.EXPIRED.getCode());
        //当前可用的优惠券个数一定是大于1的
        assert curUsableCoupons.size() > coupons.size();
        coupons.forEach(c->needCachedExpired
                .put(c.getId().toString(),JSON.toJSONString(c)));
        //校验当前的优惠券是否参与Cache 中的匹配
        List<Integer> curUsableIds =
                curUsableCoupons.stream().map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
        if(!org.apache.commons.collections4.CollectionUtils.isSubCollection(paramIds,curUsableIds)){
            log.error("CurCoupon Is Not Equal To Cache{},{},{}",
                    userId,JSON.toJSONString(curUsableIds),JSON.toJSONString(paramIds));
            throw new CouponException("CurCoupons Is Not Equal To Cache");
        }
        List<String> needCleanKey =
                paramIds.stream().map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //已过期的优惠券 Cache 缓存
                operations.opsForHash().putAll(redisKeyForExpired,needCachedExpired);
                //可用的优惠券 Cache需要清理
                operations.opsForHash().delete(redisKeyForUsable,needCleanKey.toArray());
                //重置过期时间
                operations.expire(redisKeyForExpired,getRandomExpirrationTime(1,2),TimeUnit.SECONDS);
                operations.expire(redisKeyForUsable,getRandomExpirrationTime(1,2),TimeUnit.SECONDS);
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));

        return coupons.size();
    }

    /**
     * 将已使用的优惠券加到 Cache 中
     * @param userId
     * @param coupons
     * @return
     */
    private Integer addCouponToCacheForUsed(Long userId, List<Coupon> coupons) throws CouponException {
        //如果status是 Used ,代表用户操作是使用当前的优惠券,
        //会影响2个Cache,USABLE 和 USED
        log.info("Add Coupon To Cache For Used");
        Map<String,String> needCachedForUsed = new HashMap<>();
        String redisKeyForUsable = status2RedisKey(CouponStatus.USABLE.getCode(), userId);
        String redisKeyForUsed = status2RedisKey(CouponStatus.USED.getCode(), userId);
        //获取当前用户可用的优惠券
        List<Coupon> curUsableCoupons = getCachedCoupons(userId, CouponStatus.USABLE.getCode());
        //当前可用的优惠券个数一定是大于1的
        assert curUsableCoupons.size() > coupons.size();
        coupons.forEach(c->
                needCachedForUsed.put(c.getId().toString(),JSON.toJSONString(c)));
        //校验当前优惠券参数是否与 Cached 中的匹配
        List<Integer> curUsableIds =
                curUsableCoupons.stream().map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds =
                coupons.stream().map(Coupon::getId).collect(Collectors.toList());
        if(!org.apache.commons.collections4.CollectionUtils.isSubCollection(paramIds,curUsableIds)){
            log.error("CurCoupon Is Not Equal To Cache{},{},{}",
                    userId,JSON.toJSONString(curUsableCoupons),JSON.toJSONString(paramIds));
            throw new CouponException("CurCoupons Is Not Equal To Cache");
        }
        List<String> needCleanKey =
                paramIds.stream().map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //1.已使用的优惠券Cache缓存添加
                operations.opsForHash().putAll(redisKeyForUsed,needCachedForUsed);
                //2.可用的优惠券 Cache 需要清理
                operations.opsForHash().delete(redisKeyForUsable,needCleanKey.toArray());
                //3.修改过期时间
                operations.expire(redisKeyForUsable,getRandomExpirrationTime(1,2),TimeUnit.SECONDS);
                operations.expire(redisKeyForUsed,getRandomExpirrationTime(1,2),TimeUnit.SECONDS);
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
       return coupons.size();
    }

    /**
     * 新增加优惠券到Cache 中
     */
    private Integer addCouponToCacheForUsable(Long userId,List<Coupon> coupons){
        //如果status是 Usable ,代表是新增加的优惠券
        //只会影响一个
        log.info("Add Coupon To Cache For Usable");
        Map<String,String> needCachedObject = new HashMap<>();
        coupons.forEach(c->
                needCachedObject.put(c.getId().toString(),JSON.toJSONString(c))
        );
        String redisKey = status2RedisKey(CouponStatus.USABLE.getCode(), userId);
        redisTemplate.opsForHash().putAll(redisKey,needCachedObject);

        log.info("Add {} Coupons To Cache: {},{}",needCachedObject.size(),userId,redisKey);
        //设置过期时间
        redisTemplate.expire(redisKey,getRandomExpirrationTime(1,2), TimeUnit.SECONDS);
        //存储个数
        return needCachedObject.size();
    }

    /**
     * 根据status 获取到对应的Redis Key
     */
    private String status2RedisKey(Integer status,Long userId){
        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(status);

        switch (couponStatus){
            case USABLE:
                redisKey = String.format("%s%s", Constant.RedisPrefix.USER_COUPON_USABLE, userId);
                break;
            case USED:
                redisKey=String.format("%s%s",Constant.RedisPrefix.USER_COUPON_USED,userId);
                break;
            case EXPIRED:
                redisKey=String.format("%s%s",Constant.RedisPrefix.USER_COUPON_EXPIRED,userId);
                break;
        }
        return redisKey;
    }

    /**
     * 获取一个随机的过期时间
     */
    private Long getRandomExpirrationTime(Integer min,Integer max){

        return RandomUtils.nextLong(min * 60 * 60,
                max * 60 * 60);
    }
}
