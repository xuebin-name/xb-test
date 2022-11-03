package server.nettyserver.nio;

import io.netty.util.CharsetUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName: NioFileChannelWrite
 * @Description:
 * @Author: xue
 * @Date: 2022/11/2
 */
public class NioFileChannelWrite {

    public static void main(String[] args) throws IOException {
        String str = "hello nio 测试";
        //创建输入出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\001.txt");
        //获取通道
        FileChannel channel = fileOutputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //数据写入缓冲区
        byteBuffer.put(str.getBytes(CharsetUtil.UTF_8));
        //翻转 position 因为写入字节会将position后移，写入需要 从0开始
        byteBuffer.flip();
        //将缓冲区的数据通过管道写出去
        channel.write(byteBuffer);
        fileOutputStream.close();

    }
}
