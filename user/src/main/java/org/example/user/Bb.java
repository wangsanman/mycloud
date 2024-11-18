package org.example.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Slf4j
public class Bb {

    public static void main(String[] args) {
////        Character c = '中';
//        String b = "d";
//        char c = 'd';
//
//        char aa = '中';
//        String aaa="中";
//        byte[] bytes2 = aaa.getBytes(StandardCharsets.UTF_8);
//        byte[] bytes3 = aaa.getBytes(StandardCharsets.UTF_16);
//        String s = new String(bytes3, StandardCharsets.UTF_16);
//        String s2 = new String(bytes3, StandardCharsets.UTF_8);
//        String s1 = new String(bytes3);
//        int a = 112341;
//
//        String adaf= "中国";
//
//        byte[] bytes1 = adaf.getBytes();
//
//        byte[] bytes = new byte[2];
//        bytes[0] = (byte) (c >> 8);  // 高位字节
//        bytes[1] = (byte) (c);
//
//        char ch = 'a';

        //char a = 'a';
        String kkkk = new String("\u006b");
    }

    @PostConstruct
    public String add() throws InterruptedException {
//        Thread.sleep(200);
//        do {
//            log.info("啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦");
//            log.error("坎坎坷坷看坎坎坷坷坎坎坷坷坎坎坷坷坎坎坷坷");
//            log.debug("看对方咖啡开始的饭卡是开发商卡的饭卡上看父");
//        }while (true);

        return null;
    }
}
