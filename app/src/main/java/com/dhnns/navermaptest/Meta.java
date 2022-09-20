package com.dhnns.navermaptest;

import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("is_end")
    private Boolean isEnd;
    @SerializedName("pageable_count")
    private Long pageableCount;
    @SerializedName("total_count")
    private Long totalCount;

    public Boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Boolean isEnd) {
        this.isEnd = isEnd;
    }

    public Long getPageableCount() {
        return pageableCount;
    }

    public void setPageableCount(Long pageableCount) {
        this.pageableCount = pageableCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

}
