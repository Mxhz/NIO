package nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @ClassName TestBlockingNIO
 * @Description TODO
 * @Author Mengxianghezhe
 * @Date 2020/1/17 12:53 下午
 */

public class TestBlockingNIO {
    @Test
    public void client() throws IOException {
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));



        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while(inChannel.read(buf)!=-1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        sChannel.shutdownOutput();

        //接收反馈
        int len = 0;
        while((len=sChannel.read(buf)) >0){
            buf.flip();
            System.out.println(new String(buf.array(),0,len));
            buf.clear();
        }

        inChannel.close();
        sChannel.close();

    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel sSChannel = ServerSocketChannel.open();
        FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"),StandardOpenOption.WRITE,StandardOpenOption.CREATE);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        sSChannel.bind(new InetSocketAddress(9898));

        SocketChannel socketChannel = sSChannel.accept();
        SocketAddress remoteAddress = socketChannel.getRemoteAddress();
        System.out.println(remoteAddress);
        while (socketChannel.read(buf) != -1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();

        }
//        socketChannel.shutdownOutput();

        buf.put("server accept".getBytes());
        buf.flip();
        socketChannel.write(buf);


        sSChannel.close();
        outChannel.close();
        socketChannel.close();

    }

}
