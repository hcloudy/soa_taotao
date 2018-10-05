package com.taotao.order.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;
    @Value("${TT_CART_KEY}")
    private String TT_CART_KEY;
    @Autowired
    private OrderService orderService;


    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request) {
        //首先从cookie中获取token
        /*String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        //调用sso 服务获取token中的用户信息
        if(StringUtils.isNotBlank(token)) {
            TaotaoResult result = userLoginService.getUserInfoByToken(token);
            if(result.getStatus() == 200) {
                TbUser user = (TbUser) result.getData();
                List<TbItem> cartLists = cartService.getCartLists(user.getId());
                request.setAttribute("cartList", cartLists);
            }
        }*/
        TbUser user = (TbUser) request.getAttribute("user_info");
        List<TbItem> cartLists = cartService.getCartLists(user.getId());
        request.setAttribute("cartList", cartLists);
        return "order-cart";
    }

    @RequestMapping("/order/create")
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request) {
        TbUser user = (TbUser) request.getAttribute("user_info");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //调用服务
        TaotaoResult result = orderService.createOrder(orderInfo);
        request.setAttribute("orderId",result.getData().toString());
        request.setAttribute("payment",orderInfo.getPayment());
        DateTime dateTime = new DateTime();
        DateTime time = dateTime.plusDays(3);
        request.setAttribute("date",time.toString("yyyy-MM-dd"));
        return "success";
    }
}
