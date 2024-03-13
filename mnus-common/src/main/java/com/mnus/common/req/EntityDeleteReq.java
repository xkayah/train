package com.mnus.common.req;

/**
 * 数据库实体删除请求
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/13 20:30:38
 */
public class EntityDeleteReq {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeleteReq{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
