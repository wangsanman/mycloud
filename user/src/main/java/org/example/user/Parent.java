package org.example.user;

public class Parent {
    private String parentId;
    public String parentName;
    String b = "国";

    public String getB() {
        return b;
    }

    static {
        System.out.println("静态代码快执行");
    }

//    public Parent() {
//        System.out.println("Parent constructor");
//    }
}
