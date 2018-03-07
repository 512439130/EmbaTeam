package com.fala.emba.team.bean;

/**
 * Created by Administrator on 2017/12/21.
 */

public class SmsComm {

    /**
     * msgCode : 894954
     * statusCode : 1
     * message : 操作成功
     */

    private String msgCode;
    private int statusCode;
    private String message;

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
