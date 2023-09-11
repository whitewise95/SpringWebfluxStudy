package ch07_JavaNIO.soket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JavaNIoBlockServer {

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));

            while (true){
                SocketChannel channel = serverSocket.accept();
                ByteBuffer requestBodyByteBuffer = ByteBuffer.allocateDirect(1024);
                channel.read(requestBodyByteBuffer);
                requestBodyByteBuffer.flip();

                String requestBody = StandardCharsets.UTF_8.decode(requestBodyByteBuffer).toString();
                log.info("request : {}", requestBody);

                ByteBuffer responseByteBuffer = ByteBuffer.wrap("나는 서버다".getBytes());
                channel.write(responseByteBuffer);
                channel.close();
            }


        }
    }
}
