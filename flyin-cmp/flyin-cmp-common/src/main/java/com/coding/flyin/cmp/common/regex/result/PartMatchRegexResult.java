package com.coding.flyin.cmp.common.regex.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartMatchRegexResult extends BaseRegexResult {

    public static PartMatchRegexResult instance() {
        return new PartMatchRegexResult();
    }

    private PartMatchRegexResult() {}
}
