package com.wbp.mallchat.common.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BusinessErrorEnum implements ErrorEnum{
    BUSINESS_ERROR(1001, "{0}"),
    SYSTEM_ERROR(1001, "系统出小差了，请稍后再试哦~~"),;
    private Integer code;
    private String msg;
    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
