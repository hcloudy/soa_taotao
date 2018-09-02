package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCategoryList(Long parentId);

    TaotaoResult createContentCategory(Long parentId,String name);

    TaotaoResult deleteContentCategory(Long id);
}
