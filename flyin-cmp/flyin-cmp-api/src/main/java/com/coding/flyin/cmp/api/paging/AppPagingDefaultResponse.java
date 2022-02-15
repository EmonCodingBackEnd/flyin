package com.coding.flyin.cmp.api.paging;

import com.coding.flyin.cmp.api.AppResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * AppPagingStandardResponse的替代类，后续推荐使用.
 *
 * <p>创建时间: <font style="color:#00FFFF">20220215 13:29</font><br>
 *
 * @author emon
 * @version 0.1.57
 * @since 0.1.57
 */
@Getter
@Setter
public class AppPagingDefaultResponse<O, E> extends AppResponse<AppPagingData<O, E>> {

    private static final long serialVersionUID = 3769607917657547219L;

    public void setData(PagingProp pagingProp, List<E> results) {
        setData(pagingProp, results, null);
    }

    public void setData(PagingProp pagingProp, List<E> results, O extraData) {
        AppPagingData<O, E> pagingData = new AppPagingData<>();
        pagingData.setPaging(pagingProp);
        pagingData.setExtraData(extraData);
        pagingData.setResults(results);
        setData(pagingData);
    }

    public static AppPagingDefaultResponse getDefaultResponse() {
        return new AppPagingDefaultResponse();
    }
}
