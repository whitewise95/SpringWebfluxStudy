package com.example.webflux.java.soket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class JavaNIoNonBlockMultiServer {
    private static AtomicInteger count = new AtomicInteger(0);
    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));
            serverSocket.configureBlocking(false);

            while (true) {
                SocketChannel channel = serverSocket.accept();

                if (channel == null) {
                    Thread.sleep(100);
                    continue;
                }


                CompletableFuture.runAsync(() -> handleRequest(channel), executorService);
            }
        }
    }

    @SneakyThrows
    private static void handleRequest(SocketChannel channel) {
        ByteBuffer requestBodyByteBuffer = ByteBuffer.allocateDirect(1024);
        while (channel.read(requestBodyByteBuffer) == 0) {
            log.info("Reading");
        }

        requestBodyByteBuffer.flip();
        String requestBody = StandardCharsets.UTF_8.decode(requestBodyByteBuffer).toString();
        log.info("request : {}", requestBody);

        Thread.sleep(100);
        ByteBuffer responseByteBuffer = ByteBuffer.wrap("나는 서버다".getBytes());
        channel.write(responseByteBuffer);
        channel.close();
        int i = count.incrementAndGet();
        System.out.println(i);
    }
}
