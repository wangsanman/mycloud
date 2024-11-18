package org.example.user;


import org.springframework.stereotype.Component;

@Component
public class Sson extends Parent {

    public final static int A = 10;

    private String name;

    public Sson(String name) {
        System.out.println("dkdkdk");
        this.name = name;
    }
//
//    static {
//        System.out.println("儿子静态");
//    }


    public Sson() {
//        System.out.println("son constructor");
    }
}
