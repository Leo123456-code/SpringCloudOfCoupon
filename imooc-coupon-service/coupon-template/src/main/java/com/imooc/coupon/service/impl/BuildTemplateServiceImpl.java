package com.imooc.coupon.service.impl;

import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entry.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IAsyncService;
import com.imooc.coupon.service.IBuildTemplateService;
import com.imooc.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 构建优惠券模板接口实现
 * created by Leo徐忠春
 * created Time 2020/2/20-4:21
 * email 1437665365@qq.com
 */
@Service
@Slf4j
public class BuildTemplateServiceImpl implements IBuildTemplateService {
    @Autowired
    private CouponTemplateDao couponTemplateDao;
    @Autowired
    private IAsyncService asyncService;

    /**
     * 创建优惠券模板
     * @param request {@link TemplateRequest} 模板信息请求对象
     * @return
     * @throws CouponException
     */
    @Override
    public CouponTemplate buildTemplate(TemplateRequest request)
            throws CouponException {
//        //参数合法效验
//        if(!request.validate()){
//           throw new CouponException("BuildTemplate Param Is Not Valid！");
//        }
        //判断同名的优惠券模板是否存在
        if(null != couponTemplateDao.findByName(request.getName())){
            throw new CouponException("有相同名字的优惠券");
        }
        //保存到数据
        CouponTemplate couponTemplate = requestToTemplate(request);
        couponTemplate.setCreateTime(new Date());
        couponTemplate = couponTemplateDao.save(couponTemplate);
        //调用异步方法
        //生成优惠券码
        asyncService.asyncConstructCouponByTemplate(couponTemplate);

        return couponTemplate;
    }

    /**
     * 将 TemplateRequest 转换为 CouponTemplate
     */
    private CouponTemplate requestToTemplate(TemplateRequest request){
        return new CouponTemplate(
                request.getName(),request.getLogo(),request.getDesc(),
                request.getCategory(),request.getProductLine(),
                request.getCount(), request.getUserId(),
                request.getTarget(),request.getRule());
    }
}
