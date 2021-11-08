package com.yang.bioDPointObject.ServerPoint;

import com.yang.bioDPointObject.Message;
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

    //序列化实例
    SerializeUtil serializeUtil;


    public ResponseToClient(ConcurrentLinkedQueue<Socket> connectedSocketList, ConcurrentHashMap<Socket,String> clientNameMap) throws IOException {
        this.serializeUtil = new SerializeUtil();
        this.connectedSocketList = connectedSocketList;
        this.clientNameMap = clientNameMap;
    }

    public ResponseToClient(Socket socket,ConcurrentHashMap<Socket,String> clientNameMap) throws IOException {
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        this.serializeUtil = new SerializeUtil();
        this.socket = socket;
        this.clientNameMap = clientNameMap;
    }

    /**
     * 完成注册
     *
     * 首先判断该用户名是否被注册
     * Socket不用判断因为它会自动覆盖（Map的去重）
     */
    public void Register(String name) throws IOException {
        log.info("响应客户端" + socket.getPort() + "的注册请求");
        if(!clientNameMap.containsValue(name)){//还未注册
            clientNameMap.put(socket,name);
        }else{
            String msg = "该用户名已经存在！请重新选择！";
            Message responseRegisterMsg = new Message();
            responseRegisterMsg.setCode(-1);
            responseRegisterMsg.setMsg(msg);
            responseRegisterMsg.setLength((long) msg.getBytes().length);

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
        }

        log.info("clientNameMap.size = " + clientNameMap.size());
        log.info("响应完毕");
    }


    /**
     * 响应获取客户端列表
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
     * 响应客户端连接状况
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
            //已注册
            message.setCode(200);
            message.setMsg("用户：" + clientNameMap.get(socket)+ "\t 您已成功连接服务器！");
        }else{
            //code = -1 表示需要注册
            message.setCode(-1);
            message.setMsg("您已成功连接服务器，但您还未注册用户名，请您输入您的用户名~");
        }
        message.setLength((long) message.getMsg().getBytes().length);
        return message;
    }
}
