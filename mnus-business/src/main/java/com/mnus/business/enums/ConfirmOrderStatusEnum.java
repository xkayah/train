package com.mnus.business.enums;

/**
 * 订单状态枚举
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/18 11:22:35
 */
public enum ConfirmOrderStatusEnum {
    INIT("I", "初始"),
    PENDING("P", "处理中"),
    SUCCESS("S", "成功"),
    FAILED("F", "失败"),
    NO_LEFT("N", "无票"),
    CANCEL("C", "取消"),

    ;

    private String code;

    private String desc;

    ConfirmOrderStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
