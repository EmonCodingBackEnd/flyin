package com.coding.flyin.cmp.api.paging;

import com.fasterxml.jackson.annotation.JsonProperty;

// @JsonInclude(JsonInclude.Include.NON_NULL)
public class CursorPagingProp extends PagingProp {

    private static final long serialVersionUID = 2458992395193291045L;

    private String cursor;

    /** 是否游标分页：对应的是关系数据库分页. */
    @JsonProperty("isCursorPaging")
    public boolean isCursorPaging() {
        return true;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
