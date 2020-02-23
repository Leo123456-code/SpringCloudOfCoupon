package com.imooc.coupon.vo;

import com.imooc.coupon.constant.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 優惠券規則對象定義
 * created by Leo徐忠春
 * created Time 2020/2/20-0:00
 * email 1437665365@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRule {
    /*優惠券過期規則*/
    private Expriration expriration;
    /*折扣*/
    private Discount discount;
    /*每個人最多領幾張的限制*/
    private Integer limitation;
    /*使用範圍: 地域+商品類型*/
    private Usage usage;
    /*權重: 可以和哪些優惠券疊加使用, 同一類的優惠券一定不能疊加
    list[] 優惠券的唯一編碼
    * */
    private String weight;

    /**
     * <h2>校验功能</h2>
     * */
    public boolean validate() {

        return expriration.validate() && discount.validate()
                && limitation > 0 && usage.validate()
                && StringUtils.isNotEmpty(weight);
    }


    /**
     * 有效期限規則
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Expriration{
        //有效期規則, 對應PeriodType 的code字段
        private Integer period;
        //有效間隔: 只對變動性有效期有效
        private Integer gap;
        //優惠券模板的失效日期,兩類規則都有效
        private Long deadline;

        public boolean validate(){
            //最簡化效驗
            return null != PeriodType.of(period)
                    && gap > 0 && deadline > 0;
        }
    }

    /**
     * 折扣,需要與類型配合決定
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Discount{
        //額度 (滿減(20),折扣 (85) ,立減(10))
        private Integer quota;
        //基准 ,需要滿多少才可用
        private Integer base;

        public boolean validate(){
            return quota > 0
                    && base > 0;
        }
    }

    /**
     * 使用範圍
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Usage{
        //省份
        private String provice;
        //城市
        private String city;
        //商品類型 ,list[文娛,生鮮,家居,全品
        private String goodsType;

        public boolean validate(){
            return StringUtils.isNotEmpty(provice)
                    && StringUtils.isNotEmpty(city)
                    && StringUtils.isNotEmpty(goodsType);
        }
    }

}
