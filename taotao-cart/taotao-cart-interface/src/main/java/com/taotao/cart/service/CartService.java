package com.taotao.cart.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

import java.util.List;

/**
 * 购物车接口
 */
public interface CartService {

    TaotaoResult addCartService(TbItem tbItem, Long userId, Integer num);

    List<TbItem> getCartLists(Long userId);

    TaotaoResult updateCartNum(Long userId, Long itemId, Integer num);

    TaotaoResult deleteCartItem(Long itemId,Long userId);
}
