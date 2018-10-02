package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserRegisterController {

    @Autowired
    private UserRegisterService userRegisterService;

    /**
     * 校验用户注册数据
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "/user/check/{param}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult userCheck(@PathVariable String param,@PathVariable Integer type) {
        TaotaoResult result = userRegisterService.checkUserData(param, type);
        return result;
    }

    /**
     * 注册
     * @param tbUser
     * @return
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser) {
        TaotaoResult result = userRegisterService.registerUser(tbUser);
        return result;
    }
}
