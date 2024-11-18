package org.example.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DD extends Parent {
    int a;
    String b = "🀄️";

    public String getB() {
        return b;
    }

    @IgnoreToken("哇咔咔")
    public String getSuperB() {

        return super.getB();
    }

    @Bean
    public String ddd() {
        return "aa";
    }

    public static void main(String[] args) {
        DD dd = new DD();
        System.out.println(dd.getB());
        System.out.println(dd.getSuperB());
    }
}
