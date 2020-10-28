package com.coding.flyin.cmp.common.regex.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 手机号码正则表达式匹配结果子类.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180424 18:09</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
public class MobileRegexResult extends BaseRegexResult {

    /** 匹配的手机号. */
    private String mobile;

    /** 手机号前3位数字. */
    private String mobileHead;

    /** 手机号后4位数字. */
    private String mobileTail;

    public static MobileRegexResult instance() {
        return new MobileRegexResult();
    }

    private MobileRegexResult() {}
}
