package com.taotao.search.listener;

import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemChangeListener implements MessageListener{
    @Autowired
    private SearchItemService searchItemService;
    @Override
    public void onMessage(Message message) {
        //判断消息是否为TextMessage类型
        if (message instanceof TextMessage) {
            //获取商品Id
            TextMessage message1 = (TextMessage) message;
            try {
                Long itemId = Long.valueOf(message1.getText());
                //通过商品Id查询商品数据
//                SearchItem item = itemSearchMapper.getItemByItemId(itemId);
                //更新索引库
                Thread.sleep(5000);
                searchItemService.updateSearchItemById(itemId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
