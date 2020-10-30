package com.coding.flyin.cmp.api.paging;

import com.coding.flyin.cmp.api.AppRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppPagingRequest<T extends AppPagingResponse> extends AppRequest<T> {

    private static final long serialVersionUID = -4266019549697726941L;

    protected int pageIndex;

    protected int pageSize = 20;

    protected int start = 0;

    protected Integer limit = 20;
}
