package com.coding.flyin.cmp.api.paging;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PageProp implements Serializable {

    private static final long serialVersionUID = 4149762063559317208L;

    int pageSize;

    long start;

    long resultCount;

    long pageIndex;

    long pageCount;

    int newCount;
}
