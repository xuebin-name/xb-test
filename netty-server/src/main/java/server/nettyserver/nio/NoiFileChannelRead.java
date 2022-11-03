package server.nettyserver.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName: NoiFileChannelRead
 * @Description:
 * @Author: xue
 * @Date: 2022/11/2
 */
public class NoiFileChannelRead
{
    public static void main(String[] args) throws IOException {
        File file = new File("d:\\001.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        //
        FileChannel channel = fileInputStream.getChannel();
        //
        ByteBuffer allocate = ByteBuffer.allocate((int)file.length());
        int read = channel.read(allocate);
        allocate.flip();
        //allocate.getChar();
        System.out.println(new String(allocate.array()));
    }
}
