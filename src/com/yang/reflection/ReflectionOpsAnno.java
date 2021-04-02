package com.yang.reflection;


import java.lang.annotation.*;
import java.lang.reflect.Field;

//练习反射操作注解
public class ReflectionOpsAnno {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {

        //通过反射获得注解
        Class c1 = Class.forName("com.yang.reflection.Student");
        Annotation[] annotations = c1.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        
        //获取注解的vlaue的值
        TableYang tableYang = (TableYang)c1.getAnnotation(TableYang.class);
        String value = tableYang.value();
        System.out.println(value);

        //获得类指定的注解(Declared才可以获取私有的)
        Field field = c1.getDeclaredField("name");
        FieldYang annotation = field.getAnnotation(FieldYang.class);
        System.out.println(annotation.columnName());
        System.out.println(annotation.type());
        System.out.println(annotation.length());
    }
}

//对应表名
@TableYang("db_student")
class Student{

    //对应表字段名
    @FieldYang(columnName = "db_id",type = "int" ,length =10)
    private int id;
    @FieldYang(columnName = "db_age",type = "int" ,length =10)
    private int age;
    @FieldYang(columnName = "db_name",type = "varchar" ,length =3)
    private String name;

    public Student(int id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

//类名的注解
@Target(ElementType.TYPE)//作用域
@Retention(RetentionPolicy.RUNTIME)//获取范围
@interface TableYang{
    String value();
}

//属性的注解
@Target(ElementType.FIELD)//作用域
@Retention(RetentionPolicy.RUNTIME)//获取范围
@interface FieldYang{
    String columnName();
    String type();
    int length();

}
