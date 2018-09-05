package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.ItemSearchService;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {

    @Autowired
    private ItemSearchService itemSearchService;

    @RequestMapping(value = "/index/import",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult importAll() {
        try {
            TaotaoResult result = itemSearchService.importAll();
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,e.getMessage());
        }
    }
}
