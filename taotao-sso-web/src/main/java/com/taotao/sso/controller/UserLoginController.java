package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.ExceptionUtil;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;
    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            TaotaoResult result = userLoginService.login(username, password);
            if (result.getStatus() == 200) {
                CookieUtils.setCookie(request, response, TT_TOKEN_KEY, result.getData().toString());
            }
            return result;

        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object getUserInfoByToken(@PathVariable String token,String callback) {
        TaotaoResult result = userLoginService.getUserInfoByToken(token);
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue value = new MappingJacksonValue(result);
            value.setJsonpFunction(callback);
            return value;
        }
        return result;
    }

    @RequestMapping(value = "/user/logout/{token}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult logout(String token) {
        TaotaoResult result = userLoginService.logout(token);
        return result;
    }
}