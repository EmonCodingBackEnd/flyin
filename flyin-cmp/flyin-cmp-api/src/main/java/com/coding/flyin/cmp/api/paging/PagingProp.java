package com.coding.flyin.cmp.api.paging;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder({
    "isCursorPaging",
    "pageIndex",
    "pageSize",
    "totalResultCount",
    "resultCount",
    "pageCount",
    "start",
    "end",
    "first",
    "last"
})
public abstract class PagingProp implements Serializable {

    private static final long serialVersionUID = 4149762063559317208L;

    /**
     * 是否游标分页
     *
     * <ul>
     *   <li>关系数据库分页，isCursorPaging=false
     *   <li>游标分页模拟的关系数据库分页，isCursorPaging=true
     *   <li>游标分页，isCursorPaging=true
     * </ul>
     */
    protected boolean isCursorPaging;

    /** 根据游标分页时，下一个游标是多少 */
    private String cursor;

    /** 页页码：从0开始. */
    protected Integer pageIndex;

    /** 分页尺寸：必须大于0. */
    protected Integer pageSize;

    /** 总数据量. */
    protected Long totalResultCount;

    /** 当前页实际数据数量：如果不是最后一页，应该和 pageSize 一样大，如果是最后一页，可能比 pageSize 小. */
    protected Long resultCount;

    /** 是否首页. */
    protected Boolean first;

    // ==================================================华丽的分割线==================================================

    public abstract boolean isCursorPaging();

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        isCursorPaging = true;
        this.cursor = cursor;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageNumber) {
        this.pageIndex = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(long totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public Long getResultCount() {
        return resultCount;
    }

    public void setResultCount(long resultCount) {
        this.resultCount = resultCount;
    }

    /** 总页数. */
    public Integer getPageCount() {
        if (getPageSize() == null || getTotalResultCount() == null) {
            return null;
        }
        return (int) Math.ceil((double) getTotalResultCount() / (double) getPageSize());
        /*return getPageIndex() <= 0
        ? 1
        : (int) Math.ceil((double) getTotalResultCount() / (double) getPageSize());*/
    }

    /** 是否首页. */
    public abstract Boolean isFirst();

    void setFirst(boolean first) {
        this.first = first;
    }

    /** 是否有下一页. */
    public abstract Boolean isLast();
}
