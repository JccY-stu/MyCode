package com.yang.reflection;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 创建对象的方式
 * 1.new
 * 2.默认无参构造器：Class.forname(),.newInstance()
 * 3.指定构造器
 */
//动态的创建对象，通过反射
public class GetNewObject {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        //获得Class对象
        Class c1 =Class.forName("com.yang.reflection.User");

        //构造一个对象
//        User user1 = (User) c1.newInstance();//本质上调用了类的无参构造器
//        System.out.println(user);

        //通过指造器创建对象
        Constructor constructor = c1.getDeclaredConstructor(String.class, int.class, int.class);
        User user2 = (User) constructor.newInstance("咩咩咩", 001, 19);
        System.out.println(user2);

        //通过反射调用普通方法
        User user3 = (User) c1.newInstance();
        //通过反射获取一个方法
        Method setName = c1.getDeclaredMethod("setName",String.class);
        /**
         * @param user3 调用方法的对象(这里体现了反字)
         * @param "大黑狗" 给方法传参
         * */
        setName.invoke(user3,"大黑狗");
        System.out.println(user3.getName());

        //通过反射操作属性
        User user4 = (User) c1.newInstance();
        Field name = c1.getDeclaredField("name");

        //不能直接操作私有属性，需要关闭程序的安全检查，setAccessible(true)
        name.setAccessible(true);
        name.set(user4,"白羊");
        System.out.println(user4.getName());
    }
}
