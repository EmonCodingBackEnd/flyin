package com.coding.flyin.cmp.api.paging;

import com.coding.flyin.cmp.api.AppResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AppPagingResponse<T> extends AppResponse<PageProp> {

    private static final long serialVersionUID = -432944935501824776L;

    protected List<T> results;
}
