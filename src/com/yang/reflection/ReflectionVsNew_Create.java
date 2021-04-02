package com.yang.reflection;


import java.util.ArrayList;
import java.util.List;

public class ReflectionVsNew_Create {

    public static void test01(){
        long startTime = System.currentTimeMillis();
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            User user = new User();
            userList.add(user);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("New方法创建个1千万对象所消耗的时间为："+(endTime-startTime)+"ms");
    }

    //通过反射来创建对象
    public static void test02() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        long startTime = System.currentTimeMillis();
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            Class userClass = Class.forName("com.yang.reflection.User");
            User user = (User) userClass.newInstance();
            userList.add(user);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("反射newInstance方法调用无参构造器创建个1千万对象所消耗的时间为："+(endTime-startTime)+"ms");
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        test01();
        System.out.println("===============================");
        test02();
    }
}
