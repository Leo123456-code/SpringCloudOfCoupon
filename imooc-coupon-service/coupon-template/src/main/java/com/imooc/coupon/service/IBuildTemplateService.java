package com.imooc.coupon.service;

import com.imooc.coupon.entry.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.TemplateRequest;

/**
 * 构建优惠券模板接口定义
 * created by Leo徐忠春
 * created Time 2020/2/20-2:58
 * email 1437665365@qq.com
 */
public interface IBuildTemplateService {
    /**
     * <h2>创建优惠券模板</h2>
     * @param request {@link TemplateRequest} 模板信息请求对象
     * @return {@link CouponTemplate} 优惠券模板实体
     * */
    CouponTemplate buildTemplate(TemplateRequest request)
            throws CouponException;
}
