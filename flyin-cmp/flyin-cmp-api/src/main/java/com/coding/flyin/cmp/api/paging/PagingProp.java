package com.coding.flyin.cmp.api.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder({
    "isCursorPaging",
    "pageIndex",
    "pageSize",
    "totalResultCount",
    "pageCount",
    "resultCount",
    "start",
    "end",
    "first",
    "last"
})
public abstract class PagingProp implements Serializable {

    private static final long serialVersionUID = 4149762063559317208L;

    // ==================================================华丽的分割线==================================================

    /** 是否游标分页：对应的是关系数据库分页. */
    @JsonProperty("isCursorPaging")
    public boolean isCursorPaging() {
        return false;
    }
}
