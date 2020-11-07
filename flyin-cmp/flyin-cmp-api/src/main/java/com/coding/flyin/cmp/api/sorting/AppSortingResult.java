package com.coding.flyin.cmp.api.sorting;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AppSortingResult {

    private Map<String, String> sortMappings = new HashMap<>();

    public void addSortMapping(String key, String value) {
        if (null != value) {
            if (null == sortMappings) {
                sortMappings = new HashMap<>();
            }
            sortMappings.put(key, value);
        }
    }

    public String getSortMapping(String key) {
        if (null != sortMappings) {
            return sortMappings.get(key);
        } else {
            return null;
        }
    }
}
