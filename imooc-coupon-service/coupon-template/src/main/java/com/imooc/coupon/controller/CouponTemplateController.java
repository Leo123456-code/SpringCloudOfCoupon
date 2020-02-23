package com.imooc.coupon.controller;
import com.alibaba.fastjson.JSON;
import com.imooc.coupon.entry.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IBuildTemplateService;
import com.imooc.coupon.service.ITemplateBaseService;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板相关的控制器
 * created by Leo徐忠春
 * created Time 2020/2/20-23:49
 * email 1437665365@qq.com
 */
@Slf4j
@RestController

public class CouponTemplateController {
    //构建优惠券模板服务
    @Autowired
    private IBuildTemplateService buildTemplateService;
    //优惠券模板基础服务
    @Autowired
    private ITemplateBaseService templateBaseService;

    /**
     * 构建优惠券模板
     * 创建优惠券模板
     * 127.0.0.1:7001/coupon-template/template/build
     * 通过网关访问：
     * 127.0.0.1:9000/imooc/coupon-template/template/build
     * 网关转发
     * @param request
     * @return
     * @throws CouponException
     */
    @PostMapping("/template/build")
    public com.imooc.coupon.entry.CouponTemplate buildTemplate
            (@RequestBody @Valid TemplateRequest request) throws CouponException {
        log.info("Build Template={}", JSON.toJSONString(request));
        return buildTemplateService.buildTemplate(request);
    }

    /**
     * 优惠券模板详情
     * 根據優惠券模板id 獲取優惠券模板信息
     * 127.0.0.1:7001/coupon-template/template/info?id=11
     * 通过网关
     * 127.0.0.1:9000/imooc/coupon-template/template/info?id=1
     * 网关进行转发
     *
     * @param id
     * @return
     */
    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id)
            throws CouponException {
        log.info("Build Template Info For={}",id);
        return templateBaseService.buildTemplateInfo(id);
    }

    /**
     * 查找所有可用的优惠券模板
     * 127.0.0.1:7001/coupon-template/template/sdk/all
     * 通过网关访问
     * 127.0.0.1:9000/imooc/coupon-template/template/sdk/all
     */
    @GetMapping("/template/sdk/all")
    public List<CouponTemplateSDK> findAllUsableTemplate(){
        log.info("Find All Usable Template");
        return templateBaseService.findAllUsableTemplate();
    }

    /**
     * 獲取模板ids 到 CouponTemplateSDK 的映射
     * 127.0.0.1:7001/coupon-template/template/sdk/info?id=1&id=2
     * 通过网关访问
     * 127.0.0.1:9000/imooc/coupon-template/template/sdk/info?id=1&id=2
     * @return
     */
    @GetMapping("template/sdk/info")
    public Map<Integer,CouponTemplateSDK> findIds2Template
    (@RequestParam("ids") Collection<Integer> ids){
        log.info("findIds2Template...");
        return templateBaseService.findIds2TemplateSDK(ids);
    }
}
