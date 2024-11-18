package org.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
public class User extends Parent {

    @Autowired
    private List<WebMvcConfigurer> configurers;
    @Autowired
    private List<Pp> pa;
    @Autowired
    Environment environment;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    private Kk kk;


    public int aa(int q) {
        return q;
    }

    public String aa(String q, int b) {
        return "d";
    }

    int a = 1;
    String b = "abc";

    public int getA() {
//        b.getClass()
        return a;
    }

    public User() {
    }

    public User(int a, String b) {
    }


    public String toString(char ch) {
        Integer c = 22;
        c = (int) ch;
        return super.toString();
    }
}
