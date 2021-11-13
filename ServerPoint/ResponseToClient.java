package com.yang.bioDPointObject.ServerPoint;

import com.yang.bioDPointObject.Entry.MsgACK;
import com.yang.bioDPointObject.Error.ErrorEnum;
import com.yang.bioDPointObject.Error.SuccessEnum;
import com.yang.bioDPointObject.Entry.Message;
import com.yang.bioDPointObject.Util.serialize.SerializeUtil;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/8 16:43
 * @Version 1.0
 */
public class ResponseToClient {

    Logger log = Logger.getLogger("ResponseToClient");

    //输出缓冲流
    BufferedOutputStream bufferedOutputStream;
    //使用一个队列来保存已经连接的客户端的Socket（已修改为线程安全）
    ConcurrentLinkedQueue<Socket> connectedSocketList;
    //对端Socket
    Socket socket;
    //使用一个HashMap 存储 <Socket,姓名>
    ConcurrentHashMap<Socket,String> clientNameMap;
    //使用一个HashMap 存储 <姓名,Socket> 方便根据接收者的名称找到对应的Socket
    ConcurrentHashMap<String,Socket> clientSocketMap;

    //序列化实例
    SerializeUtil serializeUtil;


    public ResponseToClient(ConcurrentLinkedQueue<Socket> connectedSocketList, ConcurrentHashMap<Socket,String> clientNameMap,ConcurrentHashMap<String,Socket> clientSocketMap) throws IOException {
        this.serializeUtil = new SerializeUtil();
        this.connectedSocketList = connectedSocketList;
        this.clientNameMap = clientNameMap;
        this.clientSocketMap = clientSocketMap;
    }

    public ResponseToClient(Socket socket,ConcurrentHashMap<Socket,String> clientNameMap,ConcurrentHashMap<String,Socket> clientSocketMap) throws IOException {
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
        this.clientNameMap = clientNameMap;
        this.clientSocketMap = clientSocketMap;
    }

    /**
     * 完成注册
     *
     * 首先判断该用户名是否被注册
     * Socket不用判断因为它会自动覆盖（Map的去重）
     */
    public void Register(String name) throws IOException {
        Message responseRegisterMsg = new Message();
        log.info("响应客户端" + socket.getPort() + "的注册/登录请求");
        if(!clientNameMap.containsValue(name)){//还未注册
            clientNameMap.put(socket,name);
            clientSocketMap.put(name,socket);
            responseRegisterMsg.setCode(SuccessEnum.SUCCESS_REGISTER.getResultCode());
            responseRegisterMsg.setMsg(SuccessEnum.SUCCESS_REGISTER.getResultMsg());
        }else {
            responseRegisterMsg = new Message();
            responseRegisterMsg.setCode(ErrorEnum.ALREADY_EXIT_USER.getResultCode());
            responseRegisterMsg.setMsg(ErrorEnum.ALREADY_EXIT_USER.getResultMsg());
            responseRegisterMsg.setLength((long) ErrorEnum.ALREADY_EXIT_USER.getResultMsg().getBytes().length);
        }

        //序列化
        byte[] bytes = serializeUtil.objectToByteArray(responseRegisterMsg);
        //发送
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            log.info("连接已中断!");
            e.printStackTrace();
        }

        log.info("已经登录的人数为： clientNameMap.size = " + clientNameMap.size());
        log.info("响应完毕");
    }


    /**
     * 响应 获取客户端列表
     *
     *
     * @param socket
     * @param clientNameMap
     * @throws IOException
     */
    public void responseClientList(Socket socket, ConcurrentHashMap<Socket,String> clientNameMap) throws IOException {
        log.info("响应客户端" + clientNameMap.get(socket) + "的获取聊天室用户列表请求");
        List<String> resultList = new ArrayList<String>();
        for(Map.Entry<Socket, String> entry: clientNameMap.entrySet())
        {
            //循环添加到resultList里面去
            resultList.add(entry.getValue());
        }

        //将对象列表序列化为字节数组
        Message message = new Message(-2,null,0, resultList);
        byte[] bytes = serializeUtil.objectToByteArray(message);

        //发送消息
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            log.info("连接已中断!");
            e.printStackTrace();
        }
        log.info("响应完毕");
    }

    /**
     * 响应 客户端连接状况
     *
     * 连接成功后根据情况进行反馈：
     *      1.已注册用户名,则返回""
     *      2.未注册用户名,则返回""
     *
     * @param bufferedOutputStream
     * @param socket
     */
    public void responseConnect(BufferedOutputStream bufferedOutputStream,Socket socket) throws IOException {
        try {
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //根据是否需要注册填入注册或登录消息
        Message noticeRegister = isNoticeRegister(socket);

        //序列化
        byte[] bytes = serializeUtil.objectToByteArray(noticeRegister);

        //发送
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            log.info("连接已中断!");
            e.printStackTrace();
        }
    }

    /**
     * 是否通知注册
     *
     * 如果未注册则发送注册消息
     * 否则发送成功登录消息
     */
    public Message isNoticeRegister(Socket socket){
        Message message = new Message();
        //回复之前判断该客户端是否已经注册
        if(clientNameMap.containsKey(socket)){
            //code = 202 表示已注册
            message.setCode(SuccessEnum.SUCCESS_CONNECT.getResultCode());
            message.setMsg(SuccessEnum.SUCCESS_CONNECT.getResultMsg());
        }else{
            //code = -1 表示需要注册
            message.setCode(SuccessEnum.SUCCESS_CONNECT_REGISTER.getResultCode());
            message.setMsg(SuccessEnum.SUCCESS_CONNECT_REGISTER.getResultMsg());
        }
        message.setLength((long) message.getMsg().getBytes().length);
        return message;
    }

    //可以改造成枚举类型定义错误类型
    /**
     * 反馈客户端的错误
     */
    public void responseError(Socket socket) throws IOException {

        Message message = new Message();

        message.setCode(ErrorEnum.NOT_FOUND_USER.getResultCode());
        message.setMsg(ErrorEnum.NOT_FOUND_USER.getResultMsg());

        //序列化
        byte[] bytes = serializeUtil.objectToByteArray(message);

        //发送消息
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            log.info("连接已中断!");
            e.printStackTrace();
        }
        log.info("响应完毕");
    }

    /**
     * 反馈 消息状态
     *
     * 给客户端发送 ACK
     */
    public void responseMsgStatus(Socket socket,int uuid) throws IOException {

        //将对象列表序列化为字节数组
        MsgACK msgACK = new MsgACK(100,uuid, (long) 0);
        byte[] bytes = serializeUtil.objectToByteArray(msgACK);

        //发送消息
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            log.info("连接已中断!");
            e.printStackTrace();
        }
        log.info("响应完毕");
    }

}
