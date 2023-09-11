package ch07_JavaNIO.soket;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public class JavaIoClient {

    @SneakyThrows
    public static void main(String[] args) {
        log.info("====================Start========================");
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
        }
        log.info("===================end==========================");
    }
}
