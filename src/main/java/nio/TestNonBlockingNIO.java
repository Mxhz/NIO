package nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @ClassName TestNonBlockingNIO
 * @Description TODO
 * @Author Mengxianghezhe
 * @Date 2020/1/17 5:31 下午
 */

public class TestNonBlockingNIO {
    @Test
    public void client2() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9897));

        //socketChannel.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put("client2".getBytes());
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


        socketChannel.close();


    }

    @Test
    public void client() {
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));


            socketChannel.configureBlocking(false);

            ByteBuffer buf = ByteBuffer.allocate(1024);

            buf.put("client1".getBytes());
            buf.flip();

            socketChannel.write(buf);

            buf.clear();

            Selector selector = Selector.open();
            socketChannel.register(selector,SelectionKey.OP_READ);

            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel sChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buf1 = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = sChannel.read(buf)) > 0) {
                            buf.flip();
                            System.out.println(new String(buf.array(), 0, len));
                            buf.clear();
                        }
                    }
                    selectionKey.cancel();
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


//        socketChannel.close();
//        System.out.println("234234");
//        System.out.println(socketChannel.isConnected());

    }

    @Test
    public void server() throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9898));
        //获取选择器
        Selector selector = Selector.open();
        //注册通道，并指定监听事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        //轮训式获取
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //判断具体是什么事件准备就绪
                if (selectionKey.isAcceptable()) {
//                    SocketChannel socketChannel = serverSocketChannel.accept();
                    ServerSocketChannel ssChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = ssChannel.accept();
                    socketChannel.configureBlocking(false);
                    //将该通道注册
                    socketChannel.register(selector, selectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    System.out.println(1111);
                    int len = 0;

                    while ((len = (socketChannel.read(buf))) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
//                    if (socketChannel.read(buf) == -1){
//                        selectionKey.cancel();
//                    }
                    buf.put("accepted".getBytes());
                    buf.flip();


                    socketChannel.write(buf);
//                    socketChannel.shutdownOutput();
//                    socketChannel.register(selector,selectionKey.OP_WRITE);
//                    selectionKey.cancel();
                } else if (selectionKey.isWritable()) {
                }

                iterator.remove();


            }
        }

    }
}
