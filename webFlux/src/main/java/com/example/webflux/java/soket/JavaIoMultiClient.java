package com.example.webflux.java.soket;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class JavaIoMultiClient {

    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    @SneakyThrows
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        log.info("====================Start========================");
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            completableFutures.add(CompletableFuture.runAsync(() -> {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress("localhost", 8080));

                    OutputStream out = socket.getOutputStream();
                    String requestBody = "나는 클라이언트입니다.";
                    out.write(requestBody.getBytes());
                    out.flush();


                    InputStream in = socket.getInputStream();
                    byte[] responseBytes = new byte[1024];
                    in.read(responseBytes);
                    log.info("request : {}", new String(responseBytes).trim());
                }catch (Exception e){}
            }, executorService));
        }

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        executorService.shutdown();
        log.info("===================end==========================");
        long end = System.currentTimeMillis();
        log.info("duration: {}",(end - start) / 1000.0);
    }
}
