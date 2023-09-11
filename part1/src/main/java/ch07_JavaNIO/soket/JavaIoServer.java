package ch07_JavaNIO.soket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class JavaIoServer {

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));

            while (true){
                Socket clientSocket = serverSocket.accept();

                byte[] requestBytes = new byte[1024];

                InputStream in = clientSocket.getInputStream();
                in.read(requestBytes);
                log.info("request : {}", new String(requestBytes).trim());

                OutputStream out = clientSocket.getOutputStream();
                String response = "나는 서버입니다.";
                out.write(response.getBytes());
                out.flush();
            }
        }
    }
}
