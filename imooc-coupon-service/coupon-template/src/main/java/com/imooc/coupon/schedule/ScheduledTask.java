package com.imooc.coupon.schedule;

import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entry.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * created by Leo徐忠春
 * created Time 2020/2/20-5:27
 * email 1437665365@qq.com
 */
@Slf4j
@Component
public class ScheduledTask {
    @Autowired
    private CouponTemplateDao couponTemplateDao;

    /**
     * 下线已过期的优惠券模板
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void  offLineCouponTemplate(){
        log.info("Start To Expired CouponTemplate");
        //先查出所有未过期的优惠券
        List<CouponTemplate> templates =
                couponTemplateDao.findByExpired(false);
        if(CollectionUtils.isEmpty(templates)){
            log.info("Done To Expired CouponTemplate");
            return;
        }
        Date cur = new Date();
        //长度就是所有过期的总长度
        List<CouponTemplate> list = new ArrayList<>(templates.size());
        templates.forEach(t->{
            //根据优惠券模板规则中的"过期规则" 效验模板是否过期
            TemplateRule rule = t.getRule();
            if(rule.getExpriration().getDeadline() < cur.getTime()){
                //设置过期
                t.setExpired(true);
                list.add(t);
            }
        });
        if (!CollectionUtils.isEmpty(list)){
           log.info("Expired CouponTemplate Num :{}",
                   couponTemplateDao.saveAll(list));
        }
        log.info("Done To Expired CouponTemplate");
    }
}
