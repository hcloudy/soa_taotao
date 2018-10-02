package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    @Autowired
    TbUserMapper tbUserMapper;
    @Override
    public TaotaoResult checkUserData(String param, Integer type) {
        //1.2.3分别对应用户名、电话、邮箱
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //username
        if(type == 1) {
            if(StringUtils.isEmpty(param)) {
                return TaotaoResult.ok(false);
            }
            criteria.andUsernameEqualTo(param);
        }else if(type == 2) {
            criteria.andPhoneEqualTo(param);
        }else if(type == 3) {
            criteria.andEmailEqualTo(param);
        }else {
            return TaotaoResult.build(400,"你传入的参数类型不正确");
        }
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if(null != list && list.size() > 0) {
            return TaotaoResult.ok(false);//返回false代表参数不可以用
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult registerUser(TbUser tbUser) {
        if(StringUtils.isBlank(tbUser.getUsername())) {
            return TaotaoResult.build(400,"注册失败,用户名不能为空");
        }
        if(StringUtils.isBlank(tbUser.getPassword())) {
            return TaotaoResult.build(400,"注册失败,密码不能为空");
        }
        TaotaoResult result = checkUserData(tbUser.getUsername(), 1);
        if(!(boolean)result.getData()) {
            return TaotaoResult.build(400,"注册失败,你输入的用户名已注册");
        }
        TaotaoResult result1 = checkUserData(tbUser.getPhone(), 2);
        if(!(boolean)result1.getData()) {
            return TaotaoResult.build(400,"注册失败,你输入的手机号码已注册");
        }
        /*TaotaoResult result2 = checkUserData(tbUser.getEmail(), 3);
        if(!(boolean)result2.getData()) {
            return TaotaoResult.build(400,"注册失败,你输入的邮箱已注册");
        }*/
        tbUser.setUpdated(new Date());
        tbUser.setCreated(new Date());
        String md5PWD = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(md5PWD);
        //插入数据
        tbUserMapper.insertSelective(tbUser);
        return TaotaoResult.ok();
    }
}
