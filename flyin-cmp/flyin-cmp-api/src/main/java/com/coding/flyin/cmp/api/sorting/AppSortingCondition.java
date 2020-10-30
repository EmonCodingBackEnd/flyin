package com.coding.flyin.cmp.api.sorting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface AppSortingCondition {

    List<SortProperty> sortProps = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SortProperty implements Serializable {
        private static final long serialVersionUID = -4574532662176814002L;

        private String sortName;
        private String sortOrder;
    }

    default void addSortProp(String sortName, String sortOrder) {
        if (null != sortName && sortOrder != null) {
            sortProps.add(new SortProperty(sortName, sortOrder));
        }
    }

    default SortProperty getSortProp(String sortName) {
        if (sortName != null) {
            for (SortProperty sortProp : sortProps) {
                if (sortName.equals(sortProp.getSortName())) {
                    return sortProp;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    default String toOrderSql() {
        StringBuilder orderSql = new StringBuilder();
        if (sortProps != null) {
            for (int i = 0; i < sortProps.size(); i++) {
                SortProperty sortProp = sortProps.get(i);
                String sortOrder = sortProp.getSortOrder();
                if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
                    sortProp.setSortOrder("asc");
                }
                if (i != 0) {
                    orderSql.append(" ,")
                            .append(sortProp.getSortName())
                            .append(" ")
                            .append(sortProp.getSortOrder());
                } else {
                    orderSql.append(sortProp.getSortName())
                            .append(" ")
                            .append(sortProp.getSortOrder());
                }
            }
        }
        return orderSql.toString();
    }
}
