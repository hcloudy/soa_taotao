package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * 登录接口
 */
public interface UserLoginService {

    TaotaoResult login(String username,String password);

    TaotaoResult getUserInfoByToken(String token);

    TaotaoResult logout(String token);
}
