package com.coding.flyin.cmp.api.paging;

import com.coding.flyin.cmp.api.AppRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppPagingRequest extends AppRequest {

    private static final long serialVersionUID = -4266019549697726941L;

    /** 分页页码：从0开始. */
    protected int pageIndex = 0;

    /** 分页尺寸：必须大于0. */
    protected int pageSize = 20;

    public void setPageIndex(int pageIndex) {
        // 如果当前页小于0，重置为0，0表示第一页
        this.pageIndex = Math.max(pageIndex, 0);
    }

    public void setPageSize(int pageSize) {
        // 如果页面尺寸小于1，重置为1，最小是1条数据一页;
        this.pageSize = Math.max(pageSize, 1);
    }
}
