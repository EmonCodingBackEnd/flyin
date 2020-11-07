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
}
