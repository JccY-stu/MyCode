package com.yang.bioDPointObject.Util.serialize;

import com.yang.bioDPointObject.Entry.Message;

import java.io.*;

/**
 * @Author Chengzhi
 * @Date 2021/11/7 14:02
 * @Version 1.0
 */
public class SerializeUtil {

    /**
     * 序列化
     * 将字节数组转换为  Message对象
     *
     * @param bytes 字节数组
     * @return Object 对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        //将字节数组序列化为 Message 对象
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    /**
     * 将对象序列化为字节数组
     */
        public byte[] objectToByteArray(Object message) throws IOException {
        byte[] bytes;
        //将对象序列化为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(message);
        return bytes = baos.toByteArray();
    }

}
