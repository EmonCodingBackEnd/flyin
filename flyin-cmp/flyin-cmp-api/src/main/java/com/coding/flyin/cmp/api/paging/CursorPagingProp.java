package com.coding.flyin.cmp.api.paging;

// @JsonInclude(JsonInclude.Include.NON_NULL)
public class CursorPagingProp extends PagingProp {

    private static final long serialVersionUID = 2458992395193291045L;

    private String cursor;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
