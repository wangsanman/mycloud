package org.example.order.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;

public class OrderController {
    // 是否关闭Server
    private static boolean shutdown = false;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));

        // 循环等待一个Request请求
        while (!shutdown) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                // 创建socket
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                byte[] buffer = new byte[2048];

                input.read(buffer);

                String s = new String(buffer, "UTF-8");
//                StringBuffer stringBuffer = new StringBuffer(2048);
//                for (byte b : buffer) {
//                    stringBuffer.append((char)b);
//                }

                int i = s.indexOf("\r\n");
                String substring = s.substring(0, i + 2);
                String decode = URLDecoder.decode(substring, "UTF-8");
                System.out.println("打印\n" + s);
                // 关闭socket
                socket.close();

                // 如果接受的是关闭请求，则设置关闭监听request的标志
//                shutdown = request.getUri().equals("/SHUTDOWN");
//
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

    }
}
