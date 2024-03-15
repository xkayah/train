package com.mnus.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;


/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
public class StationSaveReq {
    /**
     * id
     */
    private Long id;
    /**
     * 站名
     */
    @NotBlank(message = "[站名]不能为空")
    private String name;
    /**
     * 站名拼音
     */
    @NotBlank(message = "[站名拼音]不能为空")
    private String namePinyin;
    /**
     * 站名拼音首字母
     */
    @NotBlank(message = "[站名拼音首字母]不能为空")
    private String namePy;
    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getNamePy() {
        return namePy;
    }

    public void setNamePy(String namePy) {
        this.namePy = namePy;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", namePinyin=").append(namePinyin);
        sb.append(", namePy=").append(namePy);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append("]");
        return sb.toString();
    }
}
