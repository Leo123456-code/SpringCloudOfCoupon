package com.imooc.coupon.vo;

import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.constant.PeriodType;
import com.imooc.coupon.entry.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券的分类
 * created by Leo徐忠春
 * created Time 2020/2/22-8:01
 * email 1437665365@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponClassify {
    //可以使用的
    private List<Coupon> usable;
    //已使用的
    private List<Coupon> used;
    //已过期的
    private List<Coupon> expired;

    /**
     * 对当前的优惠券进行分类
     * @return
     */
    public static CouponClassify classify(List<Coupon> coupons){
        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c->{
            //判断优惠券是否过期
                    boolean isTimeExpire;
                    long curTime = new Date().getTime();
                    if(c.getTemplateSDK().getRule().getExpriration()
                            .getPeriod().equals(PeriodType.REGULAR.getCode())){
                        isTimeExpire = c.getTemplateSDK().getRule().getExpriration().
                                getDeadline()<=curTime;
                    }else {
                        isTimeExpire = DateUtils.addDays(
                                c.getAssignTime(),
                                c.getTemplateSDK().getRule().getExpriration().getGap()).getTime()<= curTime;
                    }
                    if(c.getStatus() == CouponStatus.USED){
                        used.add(c);
                    }else if(c.getStatus() == CouponStatus.EXPIRED || isTimeExpire){
                        expired.add(c);
                    }else {
                        usable.add(c);
                    }
                }
        );
        return new CouponClassify(usable,used,expired);
    }

}
