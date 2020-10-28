package com.coding.flyin.cmp.common.regex.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WholeMatchRegexResult extends BaseRegexResult {

    public static WholeMatchRegexResult instance() {
        return new WholeMatchRegexResult();
    }

    private WholeMatchRegexResult() {}
}
