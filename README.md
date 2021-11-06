# MyCode

项目组件说明:
   1.Message ---  传输消息 包括 code 和 msg 目前 code < 0 的都是服务器发送给客户端的 后期准备使用code 来区别服务端和客户端
   2.heartBeat --- 准备实现心跳
   
   服务端：全局变量目前有  
       1.使用一个队列来保存已经连接的客户端的Socket (已修改为线程安全)
          connectedSocketList
       2.使用一个HashMap 存储 <Socket,姓名> 用于记录用户注册信息
          clientNameMap
    流程：1.线程池创建一个writeUtilServer子线程 传入所有已连接的客户端的SocketList，循环反馈;
          在此子线程中 完成：a.向已连接的所有Socket发送响应消息,并加入到replyMap(在其中的表示已响应)
                            b.replay 根据是否注册返回不同的信息
                            c.实现序列化对象并发送
          2.循环监听 listen accept 等待客户端连接，每连接一个客户端就将其加入到connectedSocketList中进行统计，
          创建一个子线程用于 读取数据
          
  客户端：创建Socket与服务器通信，设置Socket连接过期时间，创建一个读线程 用于读取对端消息，在该子线程中需要完成对字节数组反序列化为
  Message对象；创建一个写线程，用于发送数据
