package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchItemDao;
import com.taotao.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemDao searchItemDao;
    @Override
    public SearchResult search(String queryString, int page, int rows) throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.setQuery(queryString);
        //设置每页显示行数
        solrQuery.setRows(rows);
        //设置分页条件
        solrQuery.setStart((page-1)*rows);
        //设置默认查询域
        solrQuery.set("df","item_title");
        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<font class=\"skcolor_ljg\">");
        solrQuery.setHighlightSimplePost("</font>");

        SearchResult result = searchItemDao.searchItemList(solrQuery);
        Long count = result.getRecordCount();
        int totalPages  = (int) (count / rows);
        if (count%rows > 0) {
            totalPages ++;
        }
        result.setTotalPages(totalPages);
        result.setCurPage(page);
        return result;
    }
}
