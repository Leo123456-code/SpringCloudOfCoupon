package com.imooc.coupon.service;

import com.imooc.coupon.entry.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 優惠券 模板基礎服務定義
 * created by Leo徐忠春
 * created Time 2020/2/20-2:27
 * email 1437665365@qq.com
 */
public interface ITemplateBaseService {
    /**
     * 根據優惠券模板id 獲取優惠券模板信息
     * @param id
     * @return
     * @throws CouponException
     */
    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;
    /**
     * 查找所有可用的優惠券模板
     */
    List<CouponTemplateSDK> findAllUsableTemplate();
    /**
     * 獲取模板ids 到 CouponTemplateSDK 的映射
     * params ids
     * Map<Key: 模板id,Value:CouponTemplateSDK
     */
    Map<Integer,CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids);
}
