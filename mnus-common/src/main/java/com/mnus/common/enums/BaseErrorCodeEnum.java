package com.mnus.common.enums;

import com.mnus.common.exception.ErrorCode;

/**
 * 通用异常码
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 10:14:26
 */
public enum BaseErrorCodeEnum implements ErrorCode {
    /**
     * undefined error
     */
    UNDEFINED(-1, "操作成功!"),
    ERROR(-2, "操作失败!"),
    UNAUTHORIZED(401, "无权访问!"),
    REQUEST_TIME_OUT(408, "系统繁忙!请稍后再试!"),
    INTERNAL_SERVER_ERROR(500, "系统内部异常!"),
    SERVICE_UNAVAILABLE(503, "服务不可用!"),
    REQ_PARAMS_NOT_VALID(10000, "请求参数错误！"),
    /**
     * system 模块异常码
     */
    SYSTEM_UNAME_ALREADY_EXISTS(20000, "账号已存在!"),
    SYSTEM_USER_IS_NOT_EXISTS(20002, "用户不存在!"),
    SYSTEM_USER_ALREADY_REGISTER(20003, "账号已注册!"),
    SYSTEM_USER_REGISTER_LOGIN_INFO_EXPIRED(20004, "验证码已过期!"),
    SYSTEM_USER_MOBILE_ALREADY_EXISTS(20005, "该手机已被注册!"),
    SYSTEM_USER_MOBILE_PASSWORD_ERROR(20006, "邮件密码错误!"),
    SYSTEM_USER_EMAIL_CODE_CANNOT_EXCEED_TIMES(20007, "邮件发送不能超过三次!"),
    SYSTEM_USER_IS_LOCKED(20009, "用户已锁定!"),
    SYSTEM_USER_USERNAME_OR_PASSWORD_ERROR(20010, "账号或密码不正确，请重新输入!"),
    SYSTEM_USER_USERNAME_IS_LOCKED(20011, "账号已锁定，6小时后解锁!"),
    SYSTEM_USER_CANNOT_UPDATE_ADMIN(20012, "仅超级管理员可操作!"),
    SYSTEM_USER_MOBILE_NOT_EXISTS(20014, "该手机未注册!"),
    SYSTEM_DEFAULT_USER_CANNOT_DELETE(20015, "系统默认用户不可删除!"),
    SYSTEM_DEFAULT_ROLE_CANNOT_DELETE(20016, "系统默认角色不可删除!"),
    SYSTEM_ROLE_NOT_EXISTS(20017, "用户绑定角色异常，请联系管理员!"),
    DATASET_ADMIN_PERMISSION_ERROR(1310, "无此权限,请联系管理员"),

    /**
     * ucenter 模块异常码
     */
    UCENTER_GET_CODE_FREQUENTLY(30001, "获取验证码过于频繁，请稍后再试!"),
    UCENTER_ACCOUNT_OR_CODE_ERROR(30008, "账号或验证码错误，请重新输入!"),
    UCENTER_CODE_IS_NOT_EXISTS(30002, "请先获取验证码!"),
    UCENTER_USER_TOKEN_INFO_IS_NULL(30013, "登录信息不存在!"),
    UCENTER_USER_TOKEN_INFO_EXPIRED(30013, "登录信息过期,请重新登录!"),
    UCENTER_USER_CANNOT_UPDATE_OTHER_USER(30019, "只能修改用户自己的数据!"),

    /**
     * business 模块异常码
     */
    BUSINESS_STATION_NAME_ALREADY_EXISTS(40001, "车站已存在!"),
    BUSINESS_TRAIN_CODE_ALREADY_EXISTS(40002, "车次编号已存在!"),
    BUSINESS_TRAIN_STATION_NAME_ALREADY_EXISTS(40003, "同车次站名已存在!"),
    BUSINESS_TRAIN_STATION_INDEX_ALREADY_EXISTS(40003, "同车次站序已存在!"),
    BUSINESS_TRAIN_CARRIAGE_ALREADY_EXISTS(40003, "同车次厢序已存在!"),
    BUSINESS_ORDER_INFO_NOT_VALID(40003, "订单信息错误!"),
    BUSINESS_ORDER_INFO_YDZ_NOT_EXISTS(40003, "订单信息错误!该车次一等座未开启售卖!"),
    BUSINESS_ORDER_INFO_EDZ_NOT_EXISTS(40003, "订单信息错误!该车次二等座未开启售卖!"),
    BUSINESS_ORDER_INFO_RW_NOT_EXISTS(40003, "订单信息错误!该车次软卧未开启售卖!"),
    BUSINESS_ORDER_INFO_YW_NOT_EXISTS(40003, "订单信息错误!该车次硬卧未开启售卖!"),
    BUSINESS_ORDER_INFO_TRAIN_CODE_NOT_EXISTS(40003, "订单信息错误!车次不存在!"),
    BUSINESS_ORDER_INFO_START_NOT_EXISTS(40003, "订单信息错误!出发站不存在!"),
    BUSINESS_ORDER_INFO_END_NOT_EXISTS(40003, "订单信息错误!终点站不存在!"),
    BUSINESS_ORDER_INFO_TICKETS_NOT_EXISTS(40003, "订单信息错误!该票不存在!"),
    BUSINESS_ORDER_INFO_DATE_NOT_VALID(40003, "订单信息错误!时间不在范围内!"),
    BUSINESS_TICKET_LEFT_ZERO(40003, "余票不足!"),
    BUSINESS_CHOOSE_SEAT_FAILED(40003, "选座失败!没有符合要求的座位!"),
    BUSINESS_GET_LOCK_LIMIT(40003, "当前请求人数过多!请稍后再试!"),

    /**
     * business 模块异常码
     */
    BATCH_ADD_JOB_SCHEDULER_ERROR(50001, "创建定时任务失败,调度异常!"),
    BATCH_ADD_JOB_CLASS_NOT_EXISTS(50002, "创建定时任务失败,任务类不存在!"),
    BATCH_PAUSE_JOB_SCHEDULER_ERROR(50003, "暂停定时任务失败,调度异常!"),
    BATCH_RESUME_JOB_SCHEDULER_ERROR(50004, "重启定时任务失败,调度异常!"),
    BATCH_RESCHEDULE_JOB_SCHEDULER_ERROR(50005, "更新定时任务失败,调度异常!"),
    BATCH_DELETE_JOB_SCHEDULER_ERROR(50006, "删除定时任务失败,调度异常!"),
    BATCH_QUERY_JOB_SCHEDULER_ERROR(50007, "查询定时任务失败,调度异常!"),


    ;

    Integer code;
    String msg;

    BaseErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BaseErrorCodeEnum{");
        sb.append("code=").append(code);
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
