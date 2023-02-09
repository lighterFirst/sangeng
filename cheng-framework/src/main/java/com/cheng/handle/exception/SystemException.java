package com.cheng.handle.exception;

import com.cheng.enums.AppHttpCodeEnum;

public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //将枚举的数据传给SystemException的code和msg
    public SystemException (AppHttpCodeEnum httpCodeEnum){
        /*super 是当前对象的父对象的引用*/
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }



}
