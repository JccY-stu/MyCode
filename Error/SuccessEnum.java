package com.yang.bioDPointObject.Error;

import java.net.Socket;

/**
 * @Author Chengzhi
 * @Date 2021/11/9 14:11
 * @Version 1.0
 */
public enum SuccessEnum implements InfoInterface{
    // 数据操作成功定义
    SUCCESS(200, "成功!"),
    SUCCESS_REGISTER(201,"成功注册!"),
    SUCCESS_CONNECT(202,"您已成功连接服务器!"),
    SUCCESS_CONNECT_REGISTER(-1, "您已成功连接服务器，但您还未注册用户名，请您输入您的用户名~!");


    /** 错误码 */
    private int resultCode;

    /** 错误描述 */
    private String resultMsg;


    SuccessEnum(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public int getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }

}
