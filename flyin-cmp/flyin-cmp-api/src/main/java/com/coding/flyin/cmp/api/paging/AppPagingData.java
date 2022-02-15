package com.coding.flyin.cmp.api.paging;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页接口通用应答数据.
 *
 * <p>创建时间: <font style="color:#00FFFF">20210317 13:01</font><br>
 * 规范分页接口的应答数据，该对象实例会包裹在最外层的 data 之内。
 *
 * @author emon
 * @version 1.0.0
 * @since 0.1.0
 */
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"paging", "extraData", "results"})
public final class AppPagingData<O, E> implements Serializable {
    private static final long serialVersionUID = -2205716652505827942L;

    /** 分页属性信息. */
    protected PagingProp paging;

    /** 分页接口返回的附加信息. */
    protected O extraData;

    /** 分页接口返回的列表数据. */
    protected List<E> results;

    public AppPagingData(PagingProp paging, List<E> results) {
        if (paging != null) {
            paging.validateNoisy();
        }
        this.paging = paging;
        this.results = results;
    }

    public AppPagingData(PagingProp paging, O extraData, List<E> results) {
        if (paging != null) {
            paging.validateNoisy();
        }
        this.paging = paging;
        this.extraData = extraData;
        this.results = results;
    }

    public void setPaging(PagingProp paging) {
        if (paging != null) {
            paging.validateNoisy();
        }
        this.paging = paging;
    }
}
