package com.taotao.cart.service.impl;

import com.taotao.cart.service.CartService;
import com.taotao.cart.service.jedis.JedisClient;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${TT_CART_REDIS}")
    private String TT_CART_REDIS;

    @Override
    public TaotaoResult addCartService(TbItem tbItem, Long userId, Integer num) {
        TbItem item = queryItemByItemIdAndUserId(tbItem.getId(), userId);
        if(item != null) {
            item.setNum(item.getNum()+num);
            jedisClient.hset(TT_CART_REDIS+":"+userId+"",tbItem.getId()+"", JsonUtils.objectToJson(item));
        }else {
            tbItem.setNum(num);
            if(tbItem.getImage() != null) {
                tbItem.setImage(tbItem.getImage().split(",")[0]);
            }
            jedisClient.hset(TT_CART_REDIS+":"+userId+"",tbItem.getId()+"", JsonUtils.objectToJson(tbItem));
        }
        return TaotaoResult.ok();
    }

    private TbItem queryItemByItemIdAndUserId(Long itemId,Long userId) {
        String str = jedisClient.hget(TT_CART_REDIS + ":" + userId + "", itemId + "");
        if(StringUtils.isNotBlank(str)) {
            TbItem tbItem = JsonUtils.jsonToPojo(str, TbItem.class);
            return tbItem;
        }
        return null;
    }

    @Override
    public List<TbItem> getCartLists(Long userId) {
        Map<String, String> map = jedisClient.hgetAll(TT_CART_REDIS + ":" + userId + "");
        List<TbItem> list = new ArrayList<>();
        if(map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String json = entry.getValue();
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                list.add(tbItem);
            }
        }
        return list;
    }

    @Override
    public TaotaoResult updateCartNum(Long userId, Long itemId, Integer num) {
        TbItem tbItem = queryItemByItemIdAndUserId(itemId, userId);
        if(tbItem != null) {
            tbItem.setNum(num);
            jedisClient.hset(TT_CART_REDIS+":"+userId+"",itemId+"",JsonUtils.objectToJson(tbItem));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteCartItem(Long itemId, Long userId) {
        jedisClient.hdel(TT_CART_REDIS+":"+userId+"",itemId+"");
        return TaotaoResult.ok();
    }
}
