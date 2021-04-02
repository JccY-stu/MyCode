package com.yang.reflection;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

//什么叫反射
public class Test extends Object{
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //通过反射获取类的Class对象
        Class<?> c1 = Class.forName("com.yang.reflection.User");
        System.out.println(c1);
        for (Field field : c1.getFields()) {
            System.out.println(field);
        }

//        Class<?> c2 = Class.forName("com.yang.reflection.Test");
//        Class<?> c3 = Class.forName("com.yang.reflection.Test");
//        Class<?> c4 = Class.forName("com.yang.reflection.Test");
//        //一个类在内存中只有一个Class对象
//        //一个类被加载后，类的整个结构都会被封装在Class对象中
//        System.out.println(c2.hashCode());
//        System.out.println(c3.hashCode());
//        System.out.println(c4.hashCode());
    }

}
