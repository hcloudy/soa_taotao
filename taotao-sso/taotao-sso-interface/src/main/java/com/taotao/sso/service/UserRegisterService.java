package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * 用户注册接口
 */
public interface UserRegisterService {

    /**
     * 校验数据接口
     * @param param
     * @param type
     * @return
     */
    TaotaoResult checkUserData(String param,Integer type);

    /**
     * 注册用户接口
     * @return
     */
    TaotaoResult registerUser(TbUser tbUser);
}
