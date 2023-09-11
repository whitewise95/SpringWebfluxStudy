package ch07_JavaNIO.soket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class JavaNIoNonBlockServer {

    private static AtomicInteger count = new AtomicInteger(0);

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));
            serverSocket.configureBlocking(false);

            while (true){
                SocketChannel channel = serverSocket.accept();

                if (channel == null){
                    Thread.sleep(100);
                    continue;
                }

                handleRequest(channel);
            }
        }
    }

    @SneakyThrows
    private static void handleRequest(SocketChannel channel) throws IOException, InterruptedException {
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
        int i = count.incrementAndGet();
        System.out.println(i);
    }
}
