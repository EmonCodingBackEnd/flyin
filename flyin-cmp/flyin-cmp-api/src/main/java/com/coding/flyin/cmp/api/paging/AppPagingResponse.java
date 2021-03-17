package com.coding.flyin.cmp.api.paging;

import com.coding.flyin.cmp.api.AppResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页应答.
 *
 * <p>创建时间: <font style="color:#00FFFF">20210317 13:34</font><br>
 * <span color="yellow">【重要说明】</span><br>
 * 为了提供更为标准规范的分页应答，推荐泛型T为{@linkplain AppPagingData AppPagingData类}<br>
 * 或者也可以使用 {@linkplain AppPagingStandardResponse AppPagingStandardResponse}
 *
 * @author emon
 * @version 1.0.0
 * @since 0.1.19
 */
@Getter
@Setter
public class AppPagingResponse<T> extends AppResponse<T> {

    private static final long serialVersionUID = -432944935501824776L;

    /**
     * 为什么 @deprecated.
     *
     * <p>为了提供更为标准规范的分页应答，该属性后续版本会移除.<br>
     * <span color="yellow">【重要说明】</span><br>
     * 使用 AppPagingResponse 时，推荐泛型T为{@linkplain AppPagingData AppPagingData类} <br>
     * 或者也可以使用 {@linkplain AppPagingStandardResponse AppPagingStandardResponse}<br>
     *
     * <p>使用示例1：<br>
     * <hr>
     *
     * <blockquote>
     *
     * <pre>
     *     定义应答类：<br/>
     *     public class DemoResponse extends AppPagingResponse&lt;AppPagingData&gt; {}<br/>
     *     接口应答时：<br/>
     *     如果有附加数据：<br/>
     *     AppPagingData&lt;T, E&gt; pagingData = new AppPagingData<>(pagingProp, T, List&lt;E&gt;);<br/>
     *     如果没有附加数据：
     *     AppPagingData&lt;Object, E&gt; pagingData = new AppPagingData<>(pagingProp, List&lt;E&gt;);<br/>
     *     DemoResponse response = new DemoResponse();<br/>
     *     response.setData(pagingData);<br/>
     *     return response;<br/>
     * </pre>
     *
     * </blockquote>
     *
     * <hr>
     *
     * <p>使用示例2：<br>
     * <hr>
     *
     * <blockquote>
     *
     * <pre>
     *     定义应答类：<br/>
     *     public class DemoResponse extends AppPagingStandardResponse {}<br/>
     *     接口应答时：<br/>
     *     如果有附加数据：<br/>
     *     AppPagingData&lt;T, E&gt; pagingData = new AppPagingData<>(pagingProp, T, List&lt;E&gt;);<br/>
     *     如果没有附加数据：
     *     AppPagingData&lt;Object, E&gt; pagingData = new AppPagingData<>(pagingProp, List&lt;E&gt;);<br/>
     *     DemoResponse response = new DemoResponse();<br/>
     *     response.setData(pagingData);<br/>
     *     return response;<br/>
     * </pre>
     *
     * </blockquote>
     *
     * <hr>
     */
    @Deprecated protected PagingProp paging;

    public static <T> AppPagingResponse<T> getDefaultResponse() {
        return new AppPagingResponse<>();
    }
}
