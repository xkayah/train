package com.mnus.common.resp;

import java.util.List;

/**
 * 分页信息
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/13 17:16:00
 */
public class PageResp<T> {
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 数据列表
     */
    private List<T> list;
    /**
     * 总分页数
     */
    private Integer pages;

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageResp{");
        sb.append("total=").append(total);
        sb.append(", list=").append(list);
        sb.append(", pages=").append(pages);
        sb.append('}');
        return sb.toString();
    }
}
