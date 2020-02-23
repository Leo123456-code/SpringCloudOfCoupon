package com.imooc.coupon.advice;

import com.imooc.coupon.annotion.IgnoreResponseAdvice;
import com.imooc.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应
 * advice 功能增强
 * created by Leo徐忠春
 * created Time 2020/2/19-1:06
 * email 1437665365@qq.com
 */
//对RestController进行增强
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {
    /**
     *  判断是否需要对响应进行处理
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>>
                                    converterType) {
        //如果当前方法所在的类标识了 @IgnoreResponseAdvice 的注解,不需要处理
        if (returnType.getDeclaringClass().isAnnotationPresent
                (IgnoreResponseAdvice.class)){
            return false;
        }
        //如果当前方法标识了 @IgnoreResponseAdvice 的注解,不需要处理
        if(returnType.getMethod().
                isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }
        //对响应进行处理,执行 beforeBodyWrite方法
        return true;
    }

    /**
     *  响应返回之前的处理
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object object,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse serverHttpResponse) {
        //定义最终的返回对象
        CommonResponse<Object> response = new CommonResponse<>(0,"");
        //如果object 是 null,response不需要设置 data
        if (object==null){
            return response;
            //如果object 已经是 CommonResponse, 不需要再次处理
        }else if(object instanceof CommonResponse){
            response = (CommonResponse<Object>) object;
            //否则,把响应对象作为 CommonResponse 的 data 部分
        }else {
            response.setData(object);
        }
        return response;
    }
}
