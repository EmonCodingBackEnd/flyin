package com.coding.flyin.cmp.api.paging;

import com.coding.flyin.cmp.exception.AppException;
import com.coding.flyin.cmp.exception.AppStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

@JsonPropertyOrder({
    "isCursorPaging",
    "pageIndex",
    "pageSize",
    "totalResultCount",
    "resultCount",
    "pageCount",
    "start",
    "end",
    "first",
    "last"
})
@Slf4j
public class PagingProp implements Serializable {

    private static final long serialVersionUID = 4149762063559317208L;

    //    /** 创建一个非游标分页属性对象，isCursorPaging=false. */
    //    public PagingProp() {
    //        this.isCursorPaging = false;
    //    }

    /**
     * 创建一个分页属性对象.
     *
     * <p>创建时间: <font style="color:#00FFFF">20210324 18:02</font><br>
     * 如果isCursorPaging=false，则认为不是游标分页，而是一个完备的普通分页，要求
     * pageIndex,pageSize,resultCount,totalResultCount 都存在。<br>
     * 如果isCursorPaging=true，则认为是一个游标分页，不会对分页属性做特殊要求<br>
     *
     * @param isCursorPaging - 是否游标分页，true-游标分页对象；false-普通分页对象
     * @author emon
     * @since 0.1.26
     */
    @JsonCreator
    public PagingProp(@JsonProperty("isCursorPaging") boolean isCursorPaging) {
        this.isCursorPaging = isCursorPaging;
    }

    /**
     * 是否游标分页
     *
     * <ul>
     *   <li>关系数据库分页，isCursorPaging=false
     *   <li>游标分页模拟的关系数据库分页，isCursorPaging=true
     *   <li>游标分页，isCursorPaging=true
     * </ul>
     */
    protected boolean isCursorPaging;

    /** 根据游标分页时，下一个游标是多少 */
    private String cursor;

    /** 页页码：从0开始. */
    protected Integer pageIndex;

    /** 分页尺寸：必须大于0. */
    protected Integer pageSize;

    /** 总数据量. */
    protected Long totalResultCount;

    /** 当前页实际数据数量：如果不是最后一页，应该和 pageSize 一样大，如果是最后一页，可能比 pageSize 小. */
    protected Long resultCount;

    /** 是否首页. */
    protected Boolean first;

    // ==================================================华丽的分割线==================================================

    public boolean isCursorPaging() {
        return isCursorPaging;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        isCursorPaging = true;
        this.cursor = cursor;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageNumber) {
        this.pageIndex = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(long totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public Long getResultCount() {
        return resultCount;
    }

    public void setResultCount(long resultCount) {
        this.resultCount = resultCount;
    }

    /** 总页数. */
    public Integer getPageCount() {
        if (getPageSize() == null || getTotalResultCount() == null) {
            return null;
        }
        return (int) Math.ceil((double) getTotalResultCount() / (double) getPageSize());
        /*return getPageIndex() <= 0
        ? 1
        : (int) Math.ceil((double) getTotalResultCount() / (double) getPageSize());*/
    }

    private Long getOffset() {
        if (getPageIndex() == null || getPageSize() == null) {
            return null;
        }
        return (long) pageIndex * (long) pageSize;
    }

    /** 分页开始数据行：从1开始. */
    public Long getStart() {
        if (getOffset() == null) {
            return null;
        }
        return getOffset() + 1;
    }

    /** 分页截止数据行. */
    public Long getEnd() {
        if (getOffset() == null || getResultCount() == null) {
            return null;
        }
        return getOffset() + getResultCount();
    }

    /** 是否有上一页. */
    public Boolean hasPrevious() {
        if (getPageIndex() == null) {
            return null;
        }
        return getPageIndex() > 0;
    }

    /** 是否有下一页. */
    public Boolean hasNext() {
        if (getPageSize() == null || getTotalResultCount() == null) {
            return null;
        }
        return getPageIndex() + 1 < getPageCount();
    }

    /** 是否首页. */
    public Boolean isFirst() {
        if (isCursorPaging) {
            return first;
        }
        return !hasPrevious();
    }

    void setFirst(boolean first) {
        this.first = first;
    }

    /** 是否最后一页. */
    public Boolean isLast() {
        if (isCursorPaging) {
            return getCursor() == null;
        } else {
            return Objects.isNull(hasNext()) ? null : !hasNext();
        }
    }

    public void validateNoisy() {
        if (!validate()) {
            log.error(
                    "PagingProp validate invalid, if isCursorPaging=false, pageIndex,pageSize,resultCount,totalResultCount can not be null");
            throw new AppException(
                    AppStatus.S0001,
                    "PagingProp validate invalid, if isCursorPaging=false, pageIndex,pageSize,resultCount,totalResultCount can not be null");
        }
    }

    public boolean validate() {
        if (!isCursorPaging) {
            return getPageIndex() != null
                    && getPageSize() != null
                    && getResultCount() != null
                    && getTotalResultCount() != null;
        }
        return true;
    }
}

// @Slf4j
// public class PagingPropJsonDeserializer extends JsonDeserializer<PagingProp> {
//    @Override
//    public PagingProp deserialize(JsonParser p, DeserializationContext ctxt)
//            throws IOException, JsonProcessingException {
//        JsonNode node = p.getCodec().readTree(p);
//        boolean isCursorPaging =
//                node.has("isCursorPaging") && node.get("isCursorPaging").asBoolean();
//        try {
//            log.info("json={}", p.getText());
//            if (isCursorPaging) {
//                return p.getCodec().readValue(p, CursorPagingProp.class);
//            } else {
//                return p.getCodec().readValue(p, NormalPagingProp.class);
//            }
//        } catch (IOException e) {
//            log.error(
//                    String.format(
//                            "Can not deserialize %s to %s",
//                            p.getText(),
//                            isCursorPaging ? CursorPagingProp.class : NormalPagingProp.class),
//                    e);
//            throw e;
//        }
//    }
// }
