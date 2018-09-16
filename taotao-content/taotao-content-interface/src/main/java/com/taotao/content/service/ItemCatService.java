package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
	List<EasyUITreeNode> getItemCatList(long parentId);
}
