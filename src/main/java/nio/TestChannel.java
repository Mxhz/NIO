package nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;
import static sun.nio.ch.IOStatus.EOF;

/**
 * @ClassName TestChannel
 * @Description TODO
 * @Author Mengxianghezhe
 * @Date 2020/1/15 9:48 下午
 */

public class TestChannel {


    @Test
    public void test4(){
        Charset cs2 = Charset.forName("UTF-8");
        cs2.newEncoder();
    }

    @Test
    public void test3() throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        inChannel = FileChannel.open(Paths.get("1.jpg"), READ);
        outChannel = FileChannel.open(Paths.get("2.jpg"), WRITE,CREATE,READ);

        inChannel.transferTo(0,inChannel.size(),outChannel);
    }


    @Test
    public void test2() throws IOException {
        //直接缓冲区
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        inChannel = FileChannel.open(Paths.get("1.jpg"), READ);
        outChannel = FileChannel.open(Paths.get("2.jpg"), WRITE,CREATE,READ);
        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);

        inChannel.close();
        outChannel.close();


    }

    @Test
    public void test1(){
//        FileInputStream fis = new FileInputStream("1.jpg");
//        FileOutputStream fos = new FileOutputStream("2.jpg");

//        FileChannel inChannel = fis.getChannel();
//        FileChannel outChannel = fos.getChannel();

        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = FileChannel.open(Paths.get("1.jpg"), READ);
            outChannel = FileChannel.open(Paths.get("2.jpg"), WRITE,CREATE);
            ByteBuffer buf = ByteBuffer.allocateDirect(1024);
            while (inChannel.read(buf) != EOF){
                buf.flip();
                outChannel.write(buf);
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel!=null){
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outChannel!=null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}
