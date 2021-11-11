# MyCode

分大三块：客户端、服务器端、工具类

客户端：
  总体流程介绍：
      1.客户端可并发执行，即可以启动多个客户端；
      2.创建Socket，连接服务器端
      3.创建一个新线程readThread，用于循环读取对端消息，根据服务端的code执行不同操作
      （-1 注册 -2 获取聊天室用户列表 801 表示该用户不存在）
      4.睡眠主线程十秒后，请求使用sendToServer.sendOrderToServer（）请求服务器端发回当前聊天室其他用户
      5.再次睡眠主线程十秒，创建一个新线程writeThread，用于循环发送数据
  解释说明：
      本来的流程应该是客户端启动，连接服务器端，readThread启动，监听服务器发来的消息，之后根据用户需要，
      调用注册/登录方法 进行注册/登录，然后如果需要进行聊天，先获取当前聊天室的所有用户，然后创建writeThread线程，
      完成聊天则终止writeThread，
      为了简化方法的调用和终止，采用了主线程睡眠十秒后自动执行 注册--->获取当前聊天室用户--->选择接收者并发送消息的流程

服务器端：
   总体流程介绍：
      1.服务器端启动
      2.使用线程池，创建一个新线程writeServer，传入所有已连接的客户端的SocketList，循环反馈是否已经注册/登录;
      3.创建一个写入 redis 的线程 writeToRedis，该线程会在没有消息写入时不做任何事，一旦消息来了则写入Redis
      4.监听客户端连接
      5.有一个客户端连接，则创建该客户端专属的线程readUtil，用于读取该客户端发送过来的消息
      
工具类：
      1.close 包下 close 关闭socket
      2.serialize 包下 serializeUtil 和ProtostuffUtils 两种序列化方式
      3.redis包下，将消息写入redis
      
实体类：
      1.Message 使用在客户端与服务器端之间进行消息传递
      2.MessageRedis 使用在服务器端将消息写入Redis（可节省空间提高效率）
      
枚举类：
      1.ErrorEnum 定义了错误描述公用信息
      2.SuccessEnum 定义了成功描述公用信息
      3.InfoInterface 上述二者的接口
        
