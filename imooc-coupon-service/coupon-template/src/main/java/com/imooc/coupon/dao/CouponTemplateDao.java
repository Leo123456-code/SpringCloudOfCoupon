package com.imooc.coupon.dao;

import com.imooc.coupon.entry.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CouponTemplate Dao 接口定义
 * created by Leo徐忠春
 * created Time 2020/2/20-2:08
 * email 1437665365@qq.com
 */
public interface CouponTemplateDao extends
        JpaRepository<CouponTemplate,Integer> {
    //根據模板名稱查詢模板
    CouponTemplate findByName(String name);
    //根据 available 和 expired 标记查找模板记录
    //根據是否可用,是否過期查找
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available,
                                                      Boolean expired);
    //根据 expired 标记查找模板记录
    //根據是否過期查找
    List<CouponTemplate> findByExpired(Boolean expired);
}
