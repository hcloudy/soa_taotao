package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserLoginService;
import com.taotao.sso.service.jedis.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
@Service
public class UserLoginServiceImpl implements UserLoginService{
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_SESSION_EXPIRE_TIME}")
    private Integer REDIS_SESSION_EXPIRE_TIME;
    @Value("${REDIS_USER_SESSION}")
    private String REDIS_USER_SESSION;
    @Override
    public TaotaoResult login(String username, String password) {
        //校验用户名和密码是否为空
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
           return TaotaoResult.build(400,"用户名或者密码不能为空");
        }
        //先校验用户名
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if(list.size() == 0 || list == null) {
            return TaotaoResult.build(400,"你输入的用户名不存在");
        }
        TbUser user = list.get(0);
        String md5pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!user.getPassword().equals(md5pwd)) {
            return TaotaoResult.build(400,"你输入的密码不正确");
        }
        String token = UUID.randomUUID().toString().replace("-","").toUpperCase();
        user.setPassword(null);
        jedisClient.set(REDIS_USER_SESSION+":"+token, JsonUtils.objectToJson(user));
        jedisClient.expire(REDIS_USER_SESSION+":"+token,REDIS_SESSION_EXPIRE_TIME);
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserInfoByToken(String token) {
        String jsonUser = jedisClient.get(REDIS_USER_SESSION + ":" + token);
        if(StringUtils.isNotBlank(jsonUser)) {
            TbUser user = JsonUtils.jsonToPojo(jsonUser, TbUser.class);
            jedisClient.expire(REDIS_USER_SESSION+":"+token,REDIS_SESSION_EXPIRE_TIME);
            return TaotaoResult.ok(user);
        }
        return TaotaoResult.build(400,"用户已过期");
    }

    @Override
    public TaotaoResult logout(String token) {
        String jsonUser = jedisClient.get(REDIS_USER_SESSION + ":" + token);
        if(StringUtils.isNotBlank(jsonUser)) {
            jedisClient.del(REDIS_USER_SESSION + ":" + token);
            return TaotaoResult.ok();
        }
        return TaotaoResult.build(400,"删除token失败");
    }
}
