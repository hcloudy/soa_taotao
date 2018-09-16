package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {
    SearchResult search(String queryString, int page, int rows) throws Exception;

    TaotaoResult updateSearchItemById(Long itemId) throws Exception;
}
