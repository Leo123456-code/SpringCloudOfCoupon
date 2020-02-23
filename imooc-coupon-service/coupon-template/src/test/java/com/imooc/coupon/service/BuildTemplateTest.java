package com.imooc.coupon.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.coupon.TemplateApplicationTest;
import com.imooc.coupon.constant.CouponCategory;
import com.imooc.coupon.constant.DistributeTarget;
import com.imooc.coupon.constant.PeriodType;
import com.imooc.coupon.constant.ProductLine;
import com.imooc.coupon.entry.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.TemplateRequest;
import com.imooc.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sun.applet.Main;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * 构造优惠券模板服务测试
 * created by Leo徐忠春
 * created Time 2020/2/21-1:21
 * email 1437665365@qq.com
 */
@Slf4j

public class BuildTemplateTest extends TemplateApplicationTest {
    @Autowired
    private IBuildTemplateService buildTemplateService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    @Test
    public void testBuildTemplate()throws Exception {
        CouponTemplate template =
                buildTemplateService.buildTemplate(fakeTemplateRequest());
        Thread.sleep(5000);
        log.info("优惠券={}",gson.toJson(template));

    }
    private TemplateRequest fakeTemplateRequest(){
        TemplateRequest request = new TemplateRequest();
        request.setName("优惠券模板-"+new Date().getTime());
        request.setLogo("http://www.imooc.com");
        request.setDesc("这是一张优惠券模板");
        request.setCategory(CouponCategory.MANJIAN.getCode());
        request.setProductLine(ProductLine.DAMAO.getCode());
        request.setCount(100);
        request.setUserId(10001L);
        request.setTarget(DistributeTarget.SINGLE.getCode());
        //优惠券规则
        TemplateRule rule = new TemplateRule();
        //有效期
        rule.setExpriration(new TemplateRule.Expriration
                (PeriodType.SHIFT.getCode(),1, DateUtils.addDays(new Date(),
                        60).getTime()));
        //限制
        rule.setDiscount(new TemplateRule.Discount(5,1));
        rule.setUsage(new TemplateRule.Usage("湖北省","武汉市",
                JSON.toJSONString(Arrays.asList("文娱","家居","数码家电"))));
        rule.setWeight(JSON.toJSONString(Collections.EMPTY_LIST));
        request.setRule(rule);
        return request;



    }
}
