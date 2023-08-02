package com.example.webflux.java.soket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JavaNIoNonBlockServer {

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));
            serverSocket.configureBlocking(false);

            while (true){
                SocketChannel channel = serverSocket.accept();

                if (channel == null){
                    Thread.sleep(100);
                    log.info("wait");
                    continue;
                }

                ByteBuffer requestBodyByteBuffer = ByteBuffer.allocateDirect(1024);
                while (channel.read(requestBodyByteBuffer) == 0){
                    log.info("Reading");
                }

                requestBodyByteBuffer.flip();
                String requestBody = StandardCharsets.UTF_8.decode(requestBodyByteBuffer).toString();
                log.info("request : {}", requestBody);

                Thread.sleep(100);
                ByteBuffer responseByteBuffer = ByteBuffer.wrap("나는 서버다".getBytes());
                channel.write(responseByteBuffer);
                channel.close();
            }
        }
    }
}
