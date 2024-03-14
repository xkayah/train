package com.mnus.ucenter.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 12:15:08
 */
public class PassengerSaveReq {
    private Long id;

    private Long userId;

    @NotEmpty(message = "[姓名]不能为空")
    private String name;

    @NotEmpty(message = "[手机号]不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")
    private String idCard;

    @NotEmpty(message = "[旅客类型]不能为空")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PassengerSaveReq{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", idCard='").append(idCard).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
