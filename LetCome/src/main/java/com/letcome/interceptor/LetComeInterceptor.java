package com.letcome.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rjt on 16/8/15.
 */
public class LetComeInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // TODO Auto-generated method stub
//        System.out.println("preHandle------" + System.nanoTime());
        return true;
    }

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
//        System.out.println("afterCompletion------" + System.nanoTime());

    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        System.out.println("postHandle------" + System.nanoTime());
//        throw new RuntimeException("登录失效");
    }

}