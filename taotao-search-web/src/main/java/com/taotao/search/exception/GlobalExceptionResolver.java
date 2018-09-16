package com.taotao.search.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * springmvc 全局异常处理
 */
@Slf4j
public class GlobalExceptionResolver implements HandlerExceptionResolver{
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //1.一般写日志
        e.printStackTrace();
        log.info(String.format("服务报错了。%s", e.getMessage()));
        //2.短信或者邮件通知管理员
        System.out.println("发邮件或者短信");
        //3.
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message","你的网络异常，请稍后");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
