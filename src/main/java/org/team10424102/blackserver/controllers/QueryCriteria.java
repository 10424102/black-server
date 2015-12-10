package org.team10424102.blackserver.controllers;

import com.sun.org.apache.xpath.internal.operations.String;
import org.springframework.data.domain.PageRequest;

public class QueryCriteria {
    private Integer page = 0;
    private Integer size = 5;
    private String filter;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public PageRequest toPageRequest() {
        return new PageRequest(page, size);
    }
}
