package com.taotao.order.pojo;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 订单的pojo
 */
@Data
public class OrderInfo extends TbOrder implements Serializable{

    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;
}
