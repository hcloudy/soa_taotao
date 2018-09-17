package com.taotao.item.web.listener;

import com.taotao.item.web.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class GenItemPageListener implements MessageListener{
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            TextMessage message1 = (TextMessage) message;
            try {
                Long itemId = Long.valueOf(message1.getText());
                //从数据库中获取数据
                TbItem tbItem = itemService.getItemById(itemId);
                Item item = new Item(tbItem);
                TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
                //生成静态页面
                genItemHtml(item,tbItemDesc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void genItemHtml(Item item,TbItemDesc tbItemDesc) throws Exception {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("item.ftl");
        //组装数据
        Map model = new HashMap();
        model.put("item",item);
        model.put("itemDesc",tbItemDesc);

        Writer writer = new FileWriter(new File("H:/study_soft/nginx-1.8.1/html/"+item.getId()+".html"));
        template.process(model,writer);
        writer.flush();
        writer.close();
    }
}
