package com.yang.bioDPointObject.Util.close;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Author Chengzhi
 * @Date 2021/11/7 14:03
 * @Version 1.0
 */
public class CloseUtil {

    /**
     * 关闭流的正确写法
     *
     * @param closes
     */
    public static void close(Closeable... closes) {
        for (Closeable closeable : closes) {
            try {
                if(closeable != null){
                    closeable.close();
                    System.out.println("成功关闭socket");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
