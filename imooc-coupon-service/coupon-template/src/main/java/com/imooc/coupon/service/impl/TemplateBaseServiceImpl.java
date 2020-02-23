package com.imooc.coupon.service.impl;

import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entry.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.ITemplateBaseService;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 優惠券 模板基礎服務实现
 * created by Leo徐忠春
 * created Time 2020/2/20-4:56
 * email 1437665365@qq.com
 */
@Slf4j
@Service

public class TemplateBaseServiceImpl implements ITemplateBaseService {
    @Autowired
    private CouponTemplateDao couponTemplateDao;

    /**
     * 根據優惠券模板id 獲取優惠券模板信息
     * @param id
     * @return
     * @throws CouponException
     */
    @Override
    public CouponTemplate buildTemplateInfo(Integer id)
            throws CouponException {
        Optional<CouponTemplate> template = couponTemplateDao.findById(id);
        if(!template.isPresent()){
            throw new CouponException("template 不存在");
        }
        return template.get();
    }

    /**
     * 查找所有可用的優惠券模板
     * @return
     */
    @Override
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        List<CouponTemplate> templates =
                couponTemplateDao.findAllByAvailableAndExpired
                (true, false);//可用,没有过期的

        return templates.stream().map(this::template2TemplateSDK).
                collect(Collectors.toList());
    }

    /**
     * 獲取模板ids 到 CouponTemplateSDK 的映射
     *      * params ids
     *      * Map<Key: 模板id,Value:CouponTemplateSDK
     * @param ids
     * @return
     */
    @Override
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK
    (Collection<Integer> ids) {
        List<CouponTemplate> templates = couponTemplateDao.findAllById(ids);

        return templates.stream().map(this::template2TemplateSDK).
                collect(Collectors.toMap(CouponTemplateSDK::getId, Function.identity()));
    }

    /**
     * 将CouponTemplate 转换为 CouponTemplateSDK
     */
    private CouponTemplateSDK template2TemplateSDK(CouponTemplate couponTemplate){
        return new CouponTemplateSDK(
                couponTemplate.getId(),couponTemplate.getName(),couponTemplate.getLogo(),
                couponTemplate.getDesc(),couponTemplate.getCategory().getCode(),couponTemplate.getProductLine().getCode(),
                couponTemplate.getKey(),couponTemplate.getTarget().getCode(),couponTemplate.getRule());
    }
}
