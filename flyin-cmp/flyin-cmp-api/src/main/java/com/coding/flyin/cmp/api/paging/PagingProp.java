package com.coding.flyin.cmp.api.paging;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder({
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

    /** 分页页码：从0开始. */
    private int pageIndex;

    /** 分页尺寸：必须大于0. */
    private int pageSize;

    /** 总数据量. */
    private long totalResultCount;

    /** 当前页实际数据数量：如果不是最后一页，应该和 pageSize 一样大，如果是最后一页，可能比 pageSize 小. */
    private long resultCount;

    // ==================================================华丽的分割线==================================================

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalResultCount() {
        return totalResultCount;
    }

    /** 总页数. */
    public long getPageCount() {
        return pageSize <= 0 ? 1 : (int) Math.ceil((double) totalResultCount / (double) pageSize);
    }

    public long getResultCount() {
        return resultCount;
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

    public void setPageIndex(int pageNumber) {
        this.pageIndex = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalResultCount(long totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public void setResultCount(long resultCount) {
        this.resultCount = resultCount;
    }

    // ==================================================华丽的分割线==================================================

    /** 是否有上一页. */
    public boolean hasPrevious() {
        return getPageIndex() > 0;
    }

    /** 是否首页. */
    public boolean isFirst() {
        return !hasPrevious();
    }

    /** 是否有下一页. */
    public boolean hasNext() {
        return getPageIndex() + 1 < getPageCount();
    }
    /** 是否最后一页. */
    public boolean isLast() {
        return !hasNext();
    }
}
