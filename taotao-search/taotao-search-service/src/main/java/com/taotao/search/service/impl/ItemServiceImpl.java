package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.ExceptionUtil;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemServiceImpl implements SearchItemService {

    @Autowired
    private SolrServer solrServer;
    @Autowired
    private SearchItemMapper searchItemMapper;

    @Override
    public TaotaoResult importAll() {
        List<SearchItem> itemList = searchItemMapper.getItemList();
        for (SearchItem item : itemList) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", item.getId());
            document.addField("item_category_name", item.getCategory_name());
            document.addField("item_image", item.getImage());
            document.addField("item_desc", item.getItem_desc());
            document.addField("item_sell_point", item.getSell_point());
            document.addField("item_title", item.getTitle());
            document.addField("item_price", item.getPrice());
            try {
                solrServer.add(document);
            } catch (Exception e) {
                e.printStackTrace();
                return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
            }
        }
        try {
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok();
    }
}
