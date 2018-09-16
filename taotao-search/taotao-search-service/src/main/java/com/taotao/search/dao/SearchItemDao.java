package com.taotao.search.dao;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

public interface SearchItemDao {

    SearchResult searchItemList(SolrQuery solrQuery) throws SolrServerException;

    TaotaoResult updateSearchItemByItemId(Long itemId) throws Exception;
}
