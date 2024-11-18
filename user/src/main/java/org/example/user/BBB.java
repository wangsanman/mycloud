package org.example.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
@Slf4j
public class BBB {
    private AAA aaa;


    public BBB toString(Integer a) {


        assert a > 0;
        BigInteger bigInteger = new BigInteger(String.valueOf(22));
        bigInteger.intValue();
        new BigDecimal("");
        int b = 0x22;
        String kk = "0x22";
        RuntimeException runtimeException = new RuntimeException("");
        Throwable cause = runtimeException.getCause();
        cause.printStackTrace();
        Optional.empty();
//        Optional.ofNullable()
//        log.info();
        return new BBB();
    }
//    private static Logger logger = LoggerFactory.getLogger(BBB.class);


    @Autowired
    public ApplicationContext applicationContext;
    @Autowired
    public Environment environment;


    static {

//        log.error("hahahahha");
//        System.out.println("我是BBB");
        Random random = new Random(2222);
        SecureRandom secureRandom = new SecureRandom();
//        for (int i = 0; i < 10; i++) {
////            System.out.println(random.nextInt());
//
//            System.out.println(secureRandom.nextInt(100));
//
//        }
    }

    public static void main(String[] args) {
        //假如泛型为Integer
        List<Integer> list = new ArrayList<>();
        list.add(1);
        test(list);//调用泛型方法
    }

    public static void test(List<? super Integer> p) {
        Object a = p.get(0);//没有任何问题
        if (a instanceof Integer) {

        }
        //但是
        Number b = new Double(22.0);
        p.add(null);

    }
}
