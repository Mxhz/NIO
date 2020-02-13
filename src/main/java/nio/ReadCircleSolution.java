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
 * @ClassName ReadCircle
 * @Description 发生循环读事件的解决方法
 * @Author Mengxianghezhe
 * @Date 2020/2/13 6:47 下午
 */


//这里解释一下原因：https://segmentfault.com/a/1190000018382477?from=singlemessage&isappinstalled=0&scene=1&clicktime=1579434258&enterid=1579434258
// 实际上就是由于返回-1导致
public class ReadCircleSolution {

    @Test
    //阻塞式客户端
    public void client() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put("BlockingClient".getBytes());
        buf.flip();

        socketChannel.write(buf);

        buf.clear();
        socketChannel.close();
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
        while(true){

            if (selector.select() > 0) {
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
                        System.out.println("read");
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        int num  = socketChannel.read(buf);
                        if (num == -1){
                            throw new IOException("读完成");
                        }

                    }
                    iterator.remove();


                }
            }
        }

    }
}
