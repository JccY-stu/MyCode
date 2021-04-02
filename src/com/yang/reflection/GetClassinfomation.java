package com.yang.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

//获得类的信息
public class GetClassinfomation{
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {

        Class<?> c1 = Class.forName("com.yang.reflection.User");

//        User user = new User();
//        c1 = user.getClass();

        //获得类的名字
        System.out.println(c1.getName());//包名+类名
        System.out.println(c1.getSimpleName());//获得类名
        System.out.println("======================");

        //获得类的属性
        Field[] fields = c1.getFields();//只能找到public属性

        fields = c1.getDeclaredFields();//找到全部的属性
        for (Field field : fields) {
            System.out.println(field);
        }

        Field name = c1.getDeclaredField("name");//通过属性名获得属性
        System.out.println(name);

        System.out.println("======================");

        //获取类的方法
        Method[] methods = c1.getMethods();//获得本类及父类的全部public方法
        for (Method method : methods) {
            System.out.println("正常的:"+method);
        }

        Method[] declaredMethods = c1.getDeclaredMethods();//获得本类的全部方法，包括私有的方法
        for (Method declaredMethod : declaredMethods) {
            System.out.println("不正常的："+declaredMethod);
        }

        System.out.println(c1.getMethod("getName", null));//获得指定方法
        /**
         * 为什么这里需要传入String.class?
         * JAVA的重载，如果方法名一样，但是无参数判断的话，它无法判断要得到哪个方法
         */
        System.out.println(c1.getMethod("setName", String.class));
        System.out.println("======================");

        //获得类构造器
        System.out.println("======================");
        Constructor[] constructors = c1.getConstructors();//获得public构造器
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }
        Constructor[] constructors2 = c1.getDeclaredConstructors();//获得本类的全部构造器
        for (Constructor constructor : constructors2) {
            System.out.println("#"+constructor);
        }

        Constructor declaredConstructor = c1.getDeclaredConstructor(String.class, int.class,int.class);
        System.out.println("指定构造器："+declaredConstructor);
    }
}
