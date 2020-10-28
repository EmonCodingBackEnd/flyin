package com.coding.flyin.cmp.common.regex.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageResult extends BaseRegexResult {

    public static ImageResult instance() {
        return new ImageResult();
    }

    private ImageResult() {}
}
