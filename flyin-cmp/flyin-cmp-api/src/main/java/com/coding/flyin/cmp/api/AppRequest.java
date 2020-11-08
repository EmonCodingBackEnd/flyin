package com.coding.flyin.cmp.api;

import com.coding.flyin.cmp.api.sorting.AppSortingCondition;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AppRequest extends AppSortingCondition implements Serializable {

    private static final long serialVersionUID = -311322238195233470L;

    /** 请求ID，用作幂等性（idempotence）校验，要求每次请求的ID不重复. */
    protected String requestId;

    /** 请求渠道，比如网关、微服务客户端、直接访问. */
    protected String requestChannel;

    /** 数字签名区域. */
    protected String signature;

    /** 加密. */
    protected boolean encryption = false;

    //    /**
    //     * 获取应答类型.
    //     *
    //     * <p>创建时间: <font style="color:#00FFFF">20190301 18:25</font><br>
    //     * [请在此输入功能详述]
    //     *
    //     * @return java.lang.Class<T>
    //     * @author Rushing0711
    //     * @since 1.0.0
    //     */
    //    @SuppressWarnings("unchecked")
    //    public Class<T> responseClass() {
    //        return (Class<T>)
    //                ((ParameterizedType) this.getClass().getGenericSuperclass())
    //                        .getActualTypeArguments()[0];
    //    }
}
