package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGrid;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
    EasyUIDataGrid getItemList(Integer page, Integer rows);

    TaotaoResult createItem(TbItem tbItem, String desc, String itemParam);

    TbItem getItemById(Long itemId);

    TbItemDesc getItemDescById(Long itemId);
}
