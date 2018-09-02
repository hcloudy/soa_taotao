package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGrid;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {

    EasyUIDataGrid getContentList(Long categoryId,Integer page, Integer rows);

    TaotaoResult createContent(TbContent tbContent);

    TaotaoResult deleteContent(String ids);

    TaotaoResult updateContent(TbContent tbContent);

    List<TbContent> getContentListByCatId(Long categoryId);

}
