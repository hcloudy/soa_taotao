package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 查询结果pojo
 */
public class SearchResult implements Serializable{

    private List<SearchItem> searchItemList;
    private Long recordCount;
    private int curPage;
    private int totalPages;

    public List<SearchItem> getSearchItemList() {
        return searchItemList;
    }

    public void setSearchItemList(List<SearchItem> searchItemList) {
        this.searchItemList = searchItemList;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
