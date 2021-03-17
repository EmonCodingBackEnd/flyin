package com.coding.flyin.cmp.common.regex.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件名匹配结果.
 *
 * <p>创建时间: <font style="color:#00FFFF">20180625 23:06</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 0.1.0
 */
@Getter
@Setter
public class FilenameResult extends BaseRegexResult {

    private String filename;

    private String prefix;

    private String suffix;

    private boolean hasSuffix;

    public static FilenameResult instance() {
        return new FilenameResult();
    }

    private FilenameResult() {}
}
