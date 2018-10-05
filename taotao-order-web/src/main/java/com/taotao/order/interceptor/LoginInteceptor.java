package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInteceptor implements HandlerInterceptor {

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;
    @Value("${LOGIN_INTERCEPTOR_URL}")
    private String LOGIN_INTERCEPTOR_URL;
    @Autowired
    private UserLoginService userLoginService;

    //进入controller之前的要做的事情，比如：做身份认证拦截
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //从cookie中获取登录用户的token
        String token = CookieUtils.getCookieValue(httpServletRequest, TT_TOKEN_KEY);
        if(StringUtils.isEmpty(token)) {
            httpServletResponse.sendRedirect(LOGIN_INTERCEPTOR_URL+"?redirect="+httpServletRequest.getRequestURL().toString());
            return false;
        }
        //调用sso服务获取用户信息。
        TaotaoResult result = userLoginService.getUserInfoByToken(token);
        //判断用户是否登录状态
        if(result.getStatus() != 200) {
            httpServletResponse.sendRedirect(LOGIN_INTERCEPTOR_URL+"?redirect="+httpServletRequest.getRequestURL().toString());
            return false;
        }
        httpServletRequest.setAttribute("user_info",result.getData());
        return true;
    }

    //返回modelAndView之前做的事情
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //返回modelAndView之后，页面视图渲染之前
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
