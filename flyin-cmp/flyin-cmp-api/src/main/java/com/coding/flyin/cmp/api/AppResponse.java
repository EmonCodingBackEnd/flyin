package com.coding.flyin.cmp.api;

import com.coding.flyin.cmp.api.sorting.AppSortingResult;
import com.coding.flyin.cmp.exception.AppStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 应答基类.
 *
 * <p>创建时间: <font style="color:#00FFFF">20201107 11:52</font><br>
 * 任何未标注 @IgnoreResponse 的类或方法，其API对外输出的信息都是标准的 AppResponse 格式，<br>
 * 也即是，仅包含以下属性信息：<br>
 *
 * <ul>
 *   <li>errorCode
 *   <li>errorMessage
 *   <li>paging(仅分页应答)
 *   <li>data
 *   <li>sortMappings
 * </ul>
 *
 * 【重要提示】除以上属性，任何其他自定义属性在应答给接口调用方时，都会被忽略！
 *
 * @author emon
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@JsonPropertyOrder({"errorCode", "errorMessage", "paging", "data", "sortMappings"})
public class AppResponse<T> extends AppSortingResult implements Serializable {

    private static final long serialVersionUID = -4627150270589944255L;

    protected String errorCode = AppStatus.SUCCESS.getErrorCode();

    protected String errorMessage = "成功";

    /** 应答结果数据. */
    protected T data;

    public static <T> AppResponse<T> getDefaultResponse() {
        return new AppResponse<>();
    }

    /** 当前应答结果是否成功状态 */
    public boolean isSuccess() {
        return AppStatus.SUCCESS.getErrorCode().equals(this.errorCode);
    }
}
