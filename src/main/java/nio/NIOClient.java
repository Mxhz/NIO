package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;

/**
 * @ClassName NIOClient
 * @Description TODO
 * @Author Mengxianghezhe
 * @Date 2020/1/20 1:34 下午
 */

public class NIOClient {
    public static void main(String[] args) throws IOException {
        client();
    }

    public static void client() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        socketChannel.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put(LocalDateTime.now().toString().getBytes());
        System.out.println(new String(LocalDateTime.now().toString().getBytes(),0,buf.position()));
        buf.flip();

        socketChannel.write(buf);

        buf.clear();

//        接收反馈
//        int len = 0;
//        while((len=socketChannel.read(buf)) >0 ){
//            buf.flip();
//            System.out.println(new String(buf.array(),0,len));
//            buf.clear();
//        }
//        try {
//            Thread.sleep(111111111);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        socketChannel.close();
//        System.out.println("234234");

    }
}
