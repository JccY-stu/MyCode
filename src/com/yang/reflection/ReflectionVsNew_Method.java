package com.yang.reflection;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试New调用对象方法和反射调用对象方法的性能
 * test01:New
 * test02:Reflection
 * test03:关闭安全检查的反射
 * 注：如不关闭安全检查则无法修改私有属性
 */
public class ReflectionVsNew_Method {

    public static void test01(){
        long startTime = System.currentTimeMillis();
        User user = new User();
        for (int i = 0; i < 1000000000; i++) {
            user.getName();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("New方法所消耗的时间为："+(endTime-startTime)+"ms");
    }

    /**
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     *
     * getDeclaredMethod(name,parameterTypes)
     * @param name 方法名
     * @param parameterTypes 方法所需要传入的参数
     *
     * method.invoke(object,args)
     * @param object 执行方法的对象
     * @param args 方法所需要传入的参数
     */
    public static void test02() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        long startTime = System.currentTimeMillis();
        User user = new User();
        Class c1= Class.forName("com.yang.reflection.User");
        Method getName = c1.getDeclaredMethod("getName", null);
        for (int i = 0; i < 1000000000; i++) {
            getName.invoke(user,null);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("反射方法所消耗的时间为："+(endTime-startTime)+"ms");
    }

    //反射方式调用，关闭检测
    public static void test03() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        long startTime = System.currentTimeMillis();
        User user = new User();
        Class c1= Class.forName("com.yang.reflection.User");
        Method getName = c1.getDeclaredMethod("getName", null);
        getName.setAccessible(true);
        for (int i = 0; i < 1000000000; i++) {
            getName.invoke(user,null);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("关闭安全检查后，反射方法所消耗的时间为："+(endTime-startTime)+"ms");
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        test01();
        System.out.println("===========================");
        test02();
        System.out.println("===========================");
        test03();
    }


}
