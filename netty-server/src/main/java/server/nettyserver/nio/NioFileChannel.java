package server.nettyserver.nio;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName: NioFileChannel
 * @Description:
 * @Author: xue
 * @Date: 2022/11/2
 */
public class NioFileChannel {

    public static void main(String[] args) throws Exception {
        mapBuffer();

    }

    private static void mapBuffer() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\001.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,0,5);
        byteBuffer.put(0,(byte) 'X');
        byteBuffer.put(3,(byte) '8');
    }

    private static void readOnly(){
        ByteBuffer allocate = ByteBuffer.allocate((int) 1);
        allocate.flip();
        ByteBuffer readOnlyBuffer = allocate.asReadOnlyBuffer();
        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }
    }

    private static void transfer ()throws Exception{
        FileInputStream inputStream = new FileInputStream("d:\\001.txt");
        FileOutputStream outputStream = new FileOutputStream("d:\\002.txt");

        FileChannel inChannel = inputStream.getChannel();
        FileChannel outChannel = outputStream.getChannel();

        outChannel.transferFrom(inChannel,0,inChannel.size());

        inChannel.close();
        outChannel.close();

        inputStream.close();
        outputStream.close();
    }

    /**
     * 读取文件内容写入另外一个文件
     * @throws IOException
     */
    private static void readAndWrite() throws IOException {
        File file = new File("d:\\001.txt");
        FileInputStream inputStream = new FileInputStream(file);
        FileOutputStream outputStream = new FileOutputStream("d:\\002.txt");

        FileChannel channel = inputStream.getChannel();
        FileChannel outchannel = outputStream.getChannel();

        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());
        while (true){
            allocate.clear();
            int read = channel.read(allocate);
            System.out.println(read);
            if(read == -1){
                break;
            }
            allocate.flip();
            System.out.println(new String(allocate.array()));
            outchannel.write(allocate);
        }

        inputStream.close();
        outputStream.close();
    }
}

