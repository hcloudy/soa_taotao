package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGrid;

public interface ItemService {
    EasyUIDataGrid getItemList(Integer page, Integer rows);
}
