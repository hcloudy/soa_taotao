package com.taotao.item.web.controller;

import com.taotao.item.web.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;

@Controller
public class ItemWebController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String getItem(@PathVariable Long itemId, Model model) {
        TbItem tbItem = itemService.getItemById(itemId);
        TbItemDesc desc = itemService.getItemDescById(itemId);
        Item item = new Item(tbItem);
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",desc);
        return "item";
    }
}
