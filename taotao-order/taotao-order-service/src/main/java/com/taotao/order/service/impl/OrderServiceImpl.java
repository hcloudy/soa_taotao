package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.order.service.jedis.JedisClient;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${GEN_ORDER_ID_KEY}")
    private String GEN_ORDER_ID_KEY;
    @Value("${GEN_ORDER_ID_VALUE}")
    private String GEN_ORDER_ID_VALUE;
    @Value("${ORDER_ITEM_ID_KEY}")
    private String ORDER_ITEM_ID_KEY;

    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
    //插入order表
        //使用redis自增策略生成主键,但是需要给一个初始主键。
        String str = jedisClient.get(GEN_ORDER_ID_KEY);
        if(StringUtils.isBlank(str)) {
            jedisClient.set(GEN_ORDER_ID_KEY,GEN_ORDER_ID_VALUE);
        }
        String orderId = jedisClient.incr(GEN_ORDER_ID_KEY).toString();
        orderInfo.setOrderId(orderId);
        //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        Date date = new Date();
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        tbOrderMapper.insert(orderInfo);
    //插入orderitem表
        //补全订单明细表
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for(TbOrderItem orderItem : orderItems) {
            String orderItemId = jedisClient.incr(ORDER_ITEM_ID_KEY).toString();
            orderItem.setId(orderItemId);
            orderItem.setOrderId(orderId);
            tbOrderItemMapper.insert(orderItem);
        }
        //插入ordershipping表
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setUpdated(date);
        orderShipping.setCreated(date);
        tbOrderShippingMapper.insert(orderShipping);
        return TaotaoResult.ok(orderId);
    }
}
