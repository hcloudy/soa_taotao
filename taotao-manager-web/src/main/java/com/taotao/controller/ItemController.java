package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGrid;
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
}
