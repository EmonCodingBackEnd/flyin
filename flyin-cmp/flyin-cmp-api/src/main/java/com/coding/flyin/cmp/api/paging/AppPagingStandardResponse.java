package com.coding.flyin.cmp.api.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppPagingStandardResponse extends AppPagingResponse<AppPagingData> {
    private static final long serialVersionUID = 7154638404168852762L;

    public static AppPagingStandardResponse getDefaultResponse() {
        return new AppPagingStandardResponse();
    }
}
