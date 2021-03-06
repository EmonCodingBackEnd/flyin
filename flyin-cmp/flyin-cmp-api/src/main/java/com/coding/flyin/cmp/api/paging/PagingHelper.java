package com.coding.flyin.cmp.api.paging;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.NonNull;

public abstract class PagingHelper {

    /**
     * 转换<br>
     * AppPagingRequest<br>
     * --><br>
     * com.baomidou.mybatisplus.extension.plugins.pagination.Page<br>
     *
     * @param request - AppPagingRequest
     * @param <T> - 分页查询应答类型
     * @return - com.baomidou.mybatisplus.extension.plugins.pagination.Page
     */
    public static <T> IPage<T> toMyBatisPlusPageCondition(AppPagingRequest request) {
        int page = Math.max(request.getPageIndex(), 0); // 如果当前页小于0，重置为0，0表示第一页
        int size = Math.max(request.getPageSize(), 1); // 如果页面尺寸小于1，重置为1，最小是1条数据一页
        // 因为MyBatisPlus的分页从1开始作为第一页，所以这里转换一下
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page + 1, size);
    }

    /**
     * 转换<br>
     * com.baomidou.mybatisplus.core.metadata.IPage<br>
     * --><br>
     * PagingProp
     *
     * @param page MyBatisPlus分页结果对象
     * @return - PagingProp
     */
    public static PagingProp fromMyBatisPlusPage(
            com.baomidou.mybatisplus.core.metadata.IPage<?> page) {
        PagingProp pagingProp = new PagingProp(false);
        pagingProp.setPageIndex((int) page.getCurrent() - 1);
        pagingProp.setPageSize((int) page.getSize());
        pagingProp.setResultCount(page.getRecords().size());
        pagingProp.setTotalResultCount(page.getTotal());
        return pagingProp;
    }

    /**
     * 转换<br>
     * org.springframework.data.domain.Page<br>
     * --><br>
     * PagingProp
     *
     * @param page JPA分页结果对象
     * @return - PagingProp
     */
    public static PagingProp fromJpaPage(org.springframework.data.domain.Page<?> page) {
        PagingProp pagingProp = new PagingProp(false);
        pagingProp.setPageIndex(page.getNumber());
        pagingProp.setPageSize(page.getSize());
        pagingProp.setResultCount(page.getNumberOfElements());
        pagingProp.setTotalResultCount(page.getTotalElements());
        return pagingProp;
    }

    /**
     * 生成一个游标分页的分页对象<br>
     * --><br>
     * PagingProp<br>
     *
     * @param isFirstQuery 游标查询是否首次查询
     * @param cursor 游标分页时的游标（比如MongoDB和ElasticSearch），如果cursor为null，表示已经到了最后一页，否则应该传入非null的值，用于下次查询的偏移量。
     * @return - PagingProp
     */
    public static PagingProp fromCursorPage(@NonNull boolean isFirstQuery, String cursor) {
        PagingProp cursorPagingProp = new PagingProp(true);
        cursorPagingProp.setCursor(cursor);
        cursorPagingProp.setFirst(isFirstQuery);
        return cursorPagingProp;
    }
}
