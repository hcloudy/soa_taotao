package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

public interface SearchItemService {
    SearchResult search(String queryString, int page, int rows) throws Exception;
}
