package com.yang.bioDPointObject.ClientPoint;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @Author Chengzhi
 * @Date 2021/11/6 16:37
 * @Version 1.0
 *
 * 客户端向服务器发送空数据包
 *
 * 心跳检测
 */
public class heartBeat implements Runnable{

    Logger log = Logger.getLogger("heartBeat");
/**
 * 当 60s 内双方无数据往来时，进行探测
 */
//如该连接在60秒内没有任何数据往来,则进行探测
private static int  KEEP_IDLE = 60;
//探测次数
private static int KEEPALIVE_PROBES = 3;
//探测超时的时间
private static  int OVER_TIME = 1000;
//心跳间隔
private static  long HEART_BEAT_RATE = 4 * 1000;
//发送时间戳
private long sendTime = 0L;

    //对端Socket
    Socket socket;
    //写入缓存流
    BufferedOutputStream bufferedOutputStream;

    public heartBeat(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
    }


    /**
     * 进行探测
     *
     * 打印对应日志
     */
    @Override
    public void run() {
        //探测次数
        int Number = KEEPALIVE_PROBES;
        while(true){
            if(Number == 0)break;
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {//每隔4秒检测一次
                System.out.println("正在检测...");
                boolean isSuccess = sendHeartBeatMsg("");
                 Number--;
                if (!isSuccess) {
                    log.info("发送心跳包未成功...正在重试");
//                mHandler.removeCallbacks(heartBeat);// 移除线程，重连时保证该线程已停止上次调用时的工作
//                mReadThread.release();//释放SocketReadThread线程资源
//                releaseLastSocket();
//                connectToServer();// 再次调用connectToServer方法，连接服务端
                }else{
                    log.info("心跳包发送成功啦~~~");
                }
            }
//        mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
        log.info("心跳包已发送完毕");
    }

    /**
     * 发送心跳包
     *
     * @param msg
     * @return
     */
    public boolean sendHeartBeatMsg(String msg) {
        if (null == socket) {
            return false;
        }
        try {
            if (!socket.isClosed() && !socket.isOutputShutdown()) {//如果Socket还未关闭
                String message = msg + "\r\n";
                bufferedOutputStream.write(message.getBytes());
                bufferedOutputStream.flush();
                sendTime = System.currentTimeMillis();//记录当前发送时间
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
