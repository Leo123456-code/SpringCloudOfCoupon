package com.imooc.coupon.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略统一响应注解定义
 * created by Leo徐忠春
 * created Time 2020/2/19-1:00
 * email 1437665365@qq.com
 */
//表示此注解可以标注在方法或类上
@Target({ElementType.TYPE,ElementType.METHOD})
//运行时的环境(运行的时候起作用)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {

}
