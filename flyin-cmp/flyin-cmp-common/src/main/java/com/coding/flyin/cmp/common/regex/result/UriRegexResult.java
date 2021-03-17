package com.coding.flyin.cmp.common.regex.result;

import lombok.Getter;
import lombok.Setter;

/**
 * Url正则匹配结果.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180611 03:08</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
@Getter
@Setter
public class UriRegexResult extends BaseRegexResult {
    
    private String uri;

    private String schema;

    private String path;

    public static UriRegexResult instance() {
        return new UriRegexResult();
    }

    private UriRegexResult() {}
}
