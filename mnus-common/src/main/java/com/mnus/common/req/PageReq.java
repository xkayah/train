package com.mnus.common.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 分页请求
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/13 16:17:05
 */
public class PageReq {
    /**
     * 当前的页码
     */
    @Min(value = 1, message = "pageNo must >= 1.")
    @Max(value = 500, message = "pageNo is too big.")
    private Integer pageNo = 1;
    /**
     * 每页的个数
     */
    @Max(value = 20, message = "pageNo must < 20.")
    private Integer pageSize = 10;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }
}
