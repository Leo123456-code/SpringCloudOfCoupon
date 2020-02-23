package com.imooc.coupon.advice;

import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * created by Leo徐忠春
 * created Time 2020/2/19-1:45
 * email 1437665365@qq.com
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {
    /**
     *  //捕获 CouponException 类的异常
     *     //@ExceptionHandler 异常处理器
     *     对  CouponException 进行统一处理
     * @param request
     * @param exception
     * @return
     */

    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handlerCouponException(
            HttpServletRequest request,CouponException exception){
        CommonResponse<String> response = new CommonResponse<>
                (-1, "business error");
        response.setData(exception.getMessage());
        return response;
    }
}
