package com.yang.reflection;

import java.lang.annotation.ElementType;

//所有类型的class
public class AllClassTest04 {
    public static void main(String[] args) {

        //类的Class
        Class c1 = Object.class;
        //接口的Class
        Class c2 = Comparable.class;
        //一维数组的Class
        Class c3 = String[].class;
        //二维数组的Class
        Class c4 = int[][].class;
        //注解类型的Class
        Class c5 = Override.class;
        //枚举类型的Class
        Class c6 = ElementType.class;
        //Class类型的Class
        Class c7 = Integer.class;
        //void类型的Class
        Class c8 = void.class;
        //Class类型的Class
        Class c9 = Class.class;

        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        System.out.println(c4);
        System.out.println(c5);
        System.out.println(c6);
        System.out.println(c7);
        System.out.println(c8);
        System.out.println(c9);

        System.out.println("===================");
        //长度不一样的数组，其Class也相同！！！同一个类型只有一份Class
        //只要元素类型与维度一样，就算同一个Class
        int[] a = new int[100];
        int[] b = new int[1000];
        System.out.println(a.getClass().hashCode());
        System.out.println(b.getClass().hashCode());
    }
}
