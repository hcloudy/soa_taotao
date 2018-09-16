package com.taotao.search.dao.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.dao.SearchItemDao;
import com.taotao.search.mapper.ItemSearchMapper;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchItemDaoImpl implements SearchItemDao{
    @Autowired
    private ItemSearchMapper itemSearchMapper;

    @Autowired
    private SolrServer solrServer;
    @Override
    public SearchResult searchItemList(SolrQuery solrQuery) throws SolrServerException {
        //通过solrQuery查询获取response
        QueryResponse response = solrServer.query(solrQuery);
        //获取查询结果集,SolrDocumentList是ArrayList的子类
        SolrDocumentList solrDocuments = response.getResults();
        List<SearchItem> itemList = new ArrayList<>();
        for (SolrDocument s : solrDocuments) {
            SearchItem item = new SearchItem();
            item.setCategory_name((String) s.get("item_category_name"));
            item.setId((String) s.get("id"));
            item.setImage((String) s.get("item_image"));
            item.setPrice((Long) s.get("item_price"));
            item.setSell_point((String) s.get("item_sell_point"));

            //获取关键字高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            String title = "";
            List<String> list = highlighting.get(s.get("id")).get("item_title");
            if (null != list && list.size() > 0) {
                title = list.get(0);
            }else {
                title = (String )s.get("item_title");
            }
            item.setTitle(title);
            itemList.add(item);
        }
        SearchResult searchResult = new SearchResult();
        searchResult.setSearchItemList(itemList);
        long numFound = solrDocuments.getNumFound();
        searchResult.setRecordCount(numFound);
        return searchResult;
    }

    /**
     * 更新索引库
     * @param itemId
     * @return
     * @throws Exception
     */
    @Override
    public TaotaoResult updateSearchItemByItemId(Long itemId) throws Exception{
        SearchItem item = itemSearchMapper.getItemByItemId(itemId);
        SolrInputDocument document = new SolrInputDocument();
        //3.向文档中添加域
        document.addField("id", item.getId());
        document.addField("item_category_name", item.getCategory_name());
        document.addField("item_image", item.getImage());
        document.addField("item_desc", item.getItem_desc());
        document.addField("item_sell_point", item.getSell_point());
        document.addField("item_title", item.getTitle());
        document.addField("item_price", item.getPrice());
        solrServer.add(document);
        solrServer.commit();
        return TaotaoResult.ok();
    }
}
