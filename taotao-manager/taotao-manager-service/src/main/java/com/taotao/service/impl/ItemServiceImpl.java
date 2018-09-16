package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGrid;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "topicDestination")
    private Destination destination;

    @Override
    public EasyUIDataGrid getItemList(Integer page, Integer rows) {
        if (null == page || 0 == page) {
            page = 1;
        }
        if (null == rows || 0 == rows) {
            rows = 30;
        }
        //PageHelper设置分页参数
        PageHelper.startPage(page, rows);
        //使用mapper查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);

        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIDataGrid result = new EasyUIDataGrid();
        result.setTotal((int) pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        return result;
    }

    @Override
    public TaotaoResult createItem(TbItem tbItem, String desc, String itemParam) {
        //生成商品ID
        final long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //插入商品表
        tbItemMapper.insert(tbItem);

        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        //插入商品描述表
        tbItemDescMapper.insert(tbItemDesc);

        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setParamData(itemParam);
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setUpdated(date);

        int insert = tbItemParamItemMapper.insert(tbItemParamItem);
        //添加发送消息给mq
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemId+"");
            }
        });
        TaotaoResult ok = TaotaoResult.ok();
        return ok;
    }

    @Override
    public TbItem getItemById(Long itemId) {
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        return item;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        return itemDesc;
    }
}
