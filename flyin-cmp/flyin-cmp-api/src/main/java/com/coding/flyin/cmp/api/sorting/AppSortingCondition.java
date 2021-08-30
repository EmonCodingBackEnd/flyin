package com.coding.flyin.cmp.api.sorting;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AppSortingCondition {

    protected List<SortProperty> sortProps;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SortProperty implements Serializable {
        private static final long serialVersionUID = -4574532662176814002L;

        private String sortName;
        private String sortOrder;
    }

    public void addSortProp(String sortName, String sortOrder) {
        if (null != sortName && sortOrder != null) {
            if (null == sortProps) {
                sortProps = new ArrayList<>();
            }
            sortProps.add(new SortProperty(sortName, sortOrder));
        }
    }

    public SortProperty getSortProp(String sortName) {
        if (null != sortProps && sortName != null) {
            for (SortProperty sortProp : sortProps) {
                if (sortName.equals(sortProp.getSortName())) {
                    return sortProp;
                }
            }
        }
        return null;
    }

    public String toOrderSql() {
        StringBuilder orderSql = new StringBuilder();
        if (sortProps != null) {
            for (int i = 0; i < sortProps.size(); i++) {
                SortProperty sortProp = sortProps.get(i);
                String sortOrder = sortProp.getSortOrder();
                if (!"asc".equals(sortOrder) && !"desc".equals(sortOrder)) {
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
