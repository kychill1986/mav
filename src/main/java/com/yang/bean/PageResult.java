package com.yang.bean;

import java.util.List;

/**
 * Created by Chill on 2016/10/25.
 */
public class PageResult {

    private long total;//总记录数

    private long page;//当前第几页

    private List<?> rows;//数据

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
