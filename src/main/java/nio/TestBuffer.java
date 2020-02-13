package nio;


import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @ClassName TestBuffer
 * @Description TODO
 * @Author Mengxianghezhe
 * @Date 2020/1/14 8:47 下午
 */

public class TestBuffer {


    @Test
    public void Test3(){
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        buf.isDirect();
    }

    @Test
    public void test2(){
        String str = "abcde";
        ByteBuffer buf = ByteBuffer.allocate(1024) ;
        buf.put(str.getBytes());

        buf.flip();

        byte[] dst = new byte[buf.limit()];
        buf.get(dst,0,2);
        System.out.println(new String(dst,0,2));
        System.out.println(buf.position());


        buf.mark();


        buf.get(dst,2,2);
        System.out.println(new String(dst,0,5));
        System.out.println(buf.position());
        buf.reset();
        System.out.println(buf.position());
    }

    @Test
    public void test1(){
        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println("=======allocate()==========");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        System.out.println("=======put()==========");

        String str = "abcde";
        buf.put(str.getBytes());
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //切换成读取数据的模式
        buf.flip();
        System.out.println("=======flip()==========");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        byte[] dst = new byte[buf.limit()];
        buf.get(dst);
        System.out.println(new String(dst,0,dst.length));
        System.out.println("=======get()==========");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        buf.rewind();
        System.out.println("=======rewind()==========");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        buf.clear();
        System.out.println("=======clear()==========");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        System.out.println((char)(buf.get()));


    }


}
