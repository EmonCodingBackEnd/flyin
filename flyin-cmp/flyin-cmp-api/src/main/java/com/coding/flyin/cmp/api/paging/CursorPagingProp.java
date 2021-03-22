//package com.coding.flyin.cmp.api.paging;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
///** 说明：分页属性归并到PagingProp类，后续该类将移除. */
//@Deprecated
//// @JsonInclude(JsonInclude.Include.NON_NULL)
//public class CursorPagingProp extends PagingProp {
//
//    private static final long serialVersionUID = 2458992395193291045L;
//
//    /** 是否游标分页：对应的是关系数据库分页. */
//    @Override
//    @JsonProperty("isCursorPaging")
//    public boolean isCursorPaging() {
//        return true;
//    }
//
//    //    @Override
//    //    public Boolean isFirst() {
//    //        return first;
//    //    }
//    //
//    //    @Override
//    //    public Boolean isLast() {
//    //        return getCursor() == null;
//    //    }
//}
