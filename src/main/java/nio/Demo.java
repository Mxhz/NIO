package nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @ClassName demo
 * @Description TODO
 * @Author Mengxianghezhe
 * @Date 2019-12-25 17:01
 */

public class Demo {
    public static void main(String[] args) throws IOException {
//        SocketChannel sc = SocketChannel.open();
        FileInputStream fileInputStream = new FileInputStream(new File("/Users/didi/Workspace/passenger2.txt"));
        FileChannel channel = fileInputStream.getChannel();
        System.out.println(channel.position());
    }
}
