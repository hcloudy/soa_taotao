package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGrid;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/item/list", method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGrid getItemList(Integer page, Integer rows) {
        EasyUIDataGrid list = itemService.getItemList(page, rows);
        return list;
    }

    @RequestMapping(value="/item/save",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createItem(TbItem tbItem, String desc, String itemParams) {
        TaotaoResult createItem = itemService.createItem(tbItem, desc, itemParams);
        return createItem;
    }
}
