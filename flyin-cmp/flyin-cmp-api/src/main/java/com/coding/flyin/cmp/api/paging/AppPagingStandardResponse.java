package com.coding.flyin.cmp.api.paging;

import lombok.Getter;
import lombok.Setter;

/**
 * 为什么@Deprecated？
 *
 * <p>该类对Swagger文档支持不够好，无法传递泛型类型；如果直接基于该类修改，会影响 response.getData().getResults() 报错，虽然依赖方调整也简单，但并不友好！
 * <br>
 * 推荐使用 {@linkplain AppPagingDefaultResponse 默认实现类 AppPagingDefaultResponse 提供了简便的setData方法}
 */
@Deprecated
@Getter
@Setter
public class AppPagingStandardResponse extends AppPagingResponse<AppPagingData> {
    private static final long serialVersionUID = 7154638404168852762L;

    public static AppPagingStandardResponse getDefaultResponse() {
        return new AppPagingStandardResponse();
    }
}
