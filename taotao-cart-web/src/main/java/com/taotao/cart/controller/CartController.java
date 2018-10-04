package com.taotao.cart.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private ItemService itemService;
    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;
    @Value("${TT_CART_KEY")
    private String TT_CART_KEY;
    @Value("${COOKIE_EXPIRE")
    private Integer COOKIE_EXPIRE;

    /**
     * 添加购物车
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId,Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 从cookie中判断用户是否登录
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        TaotaoResult result = userLoginService.getUserInfoByToken(token);
        TbItem tbItem = itemService.getItemById(itemId);
        if(result.getStatus() == 200) {
            //说明有登录
            TbUser user = (TbUser) result.getData();
            cartService.addCartService(tbItem,user.getId(),num);
        }else {
            //未登录情况下
            List<TbItem> tbItems = getCookieCartList(request);
            boolean flag = false;
            for(TbItem item : tbItems) {
                if(item.getId() == itemId) {
                    item.setNum(item.getNum()+num);
                    flag = true;
                    break;
                }
            }
            if(flag) {
                CookieUtils.setCookie(request,response,TT_CART_KEY,JsonUtils.objectToJson(tbItems),COOKIE_EXPIRE,true);
            }else {
                tbItem.setNum(num);
                if(tbItem.getImage() != null) {
                    tbItem.setImage(tbItem.getImage().split(",")[0]);
                }
                //在cookie中未找到和准备加入购物车相同的商品ID,则直接将购物车list后面add准备要加入购物车的商品
                tbItems.add(tbItem);
                CookieUtils.setCookie(request,response,TT_CART_KEY,JsonUtils.objectToJson(tbItems),COOKIE_EXPIRE,true);
            }
        }
        return "cartSuccess";
    }

    /**
     * 展示购物车商品
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request) {
        //获取用户信息
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        TaotaoResult result = userLoginService.getUserInfoByToken(token);
        if(result.getStatus() == 200) {
            TbUser user = (TbUser) result.getData();
            List<TbItem> cartLists = cartService.getCartLists(user.getId());
            request.setAttribute("cartList",cartLists);
        }else {
            List<TbItem> cookieCartList = getCookieCartList(request);
            request.setAttribute("cartList",cookieCartList);
        }
        return "cart";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemCartByItemId(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        TaotaoResult result = userLoginService.getUserInfoByToken(token);
        if(result.getStatus() == 200) {
            TbUser user = (TbUser) result.getData();
            cartService.updateCartNum(user.getId(),itemId,num);
        }else {
            updateCookieItemCart(itemId,num,request,response);
        }
        return TaotaoResult.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteItemCartByItemId(@PathVariable Long itemId,HttpServletResponse response,HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        TaotaoResult result = userLoginService.getUserInfoByToken(token);
        if(result.getStatus() == 200) {
            TbUser user = (TbUser) result.getData();
            cartService.deleteCartItem(itemId,user.getId());
        }else {
            deleteCookieItemCartByItemId(itemId,request,response);
        }
        return "redirect:/cart/cart.html";
    }


    /*-------------------------------------------完美分割线-------------------------------------------------------------*/

    private void deleteCookieItemCartByItemId(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<TbItem> cartList = getCookieCartList(request);
        boolean flag = false;
        for(TbItem item : cartList) {
            if(item.getId() == itemId) {
                cartList.remove(item);
                flag = true;
                break;
            }
        }
        if(flag) {
            CookieUtils.setCookie(request,response,TT_CART_KEY,JsonUtils.objectToJson(cartList),COOKIE_EXPIRE,true);
        }
    }
    //从cookie中获取购物车数据
    private List<TbItem> getCookieCartList(HttpServletRequest request) {
        String jsonStr = CookieUtils.getCookieValue(request, TT_CART_KEY,true);
        if(StringUtils.isNotBlank(jsonStr)) {
            List<TbItem> items = JsonUtils.jsonToList(jsonStr, TbItem.class);
            return items;
        }
        return new ArrayList<>();
    }

    private void updateCookieItemCart(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 获取购物车列表商品
        List<TbItem> cartList = getCookieCartList(request);
        boolean flag = false;
        for(TbItem item : cartList) {
            if(item.getId() == itemId) {
                item.setNum(num);
                flag = true;
                break;
            }
        }
        if(flag) {
            CookieUtils.setCookie(request,response,TT_CART_KEY,JsonUtils.objectToJson(cartList),COOKIE_EXPIRE,true);
        }
    }
}
