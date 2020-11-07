package com.coding.flyin.cmp.api.paging;

import com.coding.flyin.cmp.api.AppResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppPagingResponse<T> extends AppResponse<T> {

    private static final long serialVersionUID = -432944935501824776L;

    protected PagingProp paging;

    public static <T> AppPagingResponse<T> getDefaultResponse() {
        return new AppPagingResponse<>();
    }
}
