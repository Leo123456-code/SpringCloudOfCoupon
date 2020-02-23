package com.imooc.coupon.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.coupon.TemplateApplicationTest;
import com.imooc.coupon.entry.CouponTemplate;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板基础服务测试
 * created by Leo徐忠春
 * created Time 2020/2/21-2:33
 * email 1437665365@qq.com
 */
@Slf4j
public class TemplateBaseTest extends TemplateApplicationTest {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Autowired
    private ITemplateBaseService templateBaseService;

    @Test
    public void testTemplateBase()throws Exception {
        CouponTemplate info = templateBaseService.buildTemplateInfo(10);
        log.info("info={}",gson.toJson(info));
    }
    @Test
    public void findAllUsableTemplate() {
        List<CouponTemplateSDK> sdks = templateBaseService.findAllUsableTemplate();
        log.info("result={}",gson.toJson(sdks));
    }

    @Test
    public void findIds2TemplateSDK() {
        Map<Integer, CouponTemplateSDK> map =
                templateBaseService.findIds2TemplateSDK(Arrays.asList(10, 11, 12));
        log.info("result={}",gson.toJson(map));
    }
}
