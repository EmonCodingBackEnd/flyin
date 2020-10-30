package com.coding.flyin.cmp.api.sorting;

import java.util.HashMap;
import java.util.Map;

public interface AppSortingResult {

    Map<String, String> sortMappings = new HashMap<>();

    default void addSortMapping(String key, String value) {
        if (null != value) {
            sortMappings.put(key, value);
        }
    }

    default String getSortMapping(String key) {
        return sortMappings.get(key);
    }
}
