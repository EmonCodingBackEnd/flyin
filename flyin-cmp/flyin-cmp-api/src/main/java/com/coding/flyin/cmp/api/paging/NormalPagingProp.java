package com.coding.flyin.cmp.api.paging;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NormalPagingProp extends PagingProp {

    private static final long serialVersionUID = 1170964941403697653L;

    public NormalPagingProp() {
        isCursorPaging = false;
    }

    // ==================================================华丽的分割线==================================================

    /** 是否游标分页：对应的是关系数据库分页. */
    @Override
    @JsonProperty("isCursorPaging")
    public boolean isCursorPaging() {
        return isCursorPaging;
    }

    private long getOffset() {
        return (long) pageIndex * (long) pageSize;
    }

    /** 分页开始数据行：从1开始. */
    public long getStart() {
        return getOffset() + 1;
    }

    /** 分页截止数据行. */
    public long getEnd() {
        return getOffset() + getResultCount();
    }

    // ==================================================华丽的分割线==================================================

    /** 是否有上一页. */
    public boolean hasPrevious() {
        return getPageIndex() > 0;
    }

    /** 是否首页. */
    @Override
    public Boolean isFirst() {
        return !hasPrevious();
    }

    /** 是否有下一页. */
    public boolean hasNext() {
        return getPageIndex() + 1 < getPageCount();
    }

    /** 是否最后一页. */
    @Override
    public Boolean isLast() {
        if (isCursorPaging) {
            return getCursor() == null;
        } else {
            return !hasNext();
        }
    }
}
