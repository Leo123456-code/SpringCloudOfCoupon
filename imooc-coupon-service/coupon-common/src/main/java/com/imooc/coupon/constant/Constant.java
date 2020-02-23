package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用常量定義
 * created by Leo徐忠春
 * created Time 2020/2/20-2:41
 * email 1437665365@qq.com
 */
@Data
public class Constant {

    //kafka 消息的 topic
    public static final String TOPIC = "imooc_user_coupon_op";

    //Redis key 前綴定義
    public static class RedisPrefix{

        //優惠券碼key 的前綴
        public static final String COUPON_TEMPLATE =
                "imooc_coupon_template_code_" ;

        //用戶當前所有可用的優惠券 key前綴
        public static final String USER_COUPON_USABLE =
                "imooc_user_coupon_usable_" ;

        //用戶當前所有已使用的優惠券 key前綴
        public static final String USER_COUPON_USED =
                "imooc_user_coupon_used_" ;

        //用戶當前所有已過期的優惠券 key前綴
        public static final String USER_COUPON_EXPIRED =
                "imooc_user_coupon_expired_" ;
    }

}
