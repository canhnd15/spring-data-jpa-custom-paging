package com.davidnguyen.paging.utils.paging;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private List<T> result;

    private int currentPage;
    private int pageSize;

    private int totalPage;
    private int totalRecord;

    public Page(PageInfo pageInfo){
        this.currentPage = pageInfo.getCurrentPage();
        this.pageSize = pageInfo.getPageSize();
    }

    public Page<T> withResult(List<T> result){
        this.result = result;
        return this;
    }

    public Page<T> compute(int totalRecord){
        this.totalRecord = totalRecord;
        this.totalPage = (int) Math.ceil((double) totalRecord/pageSize);
        return this;
    }
}
