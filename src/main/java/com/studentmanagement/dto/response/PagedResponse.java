package com.studentmanagement.dto.response;

import java.util.List;

public class PagedResponse<T> {
    public List<T> items;
    public long total;
    public int page;
    public int size;

    public PagedResponse() {}

    public PagedResponse(List<T> items, long total, int page, int size) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.size = size;
    }
}
