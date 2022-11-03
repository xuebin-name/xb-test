package client.nettyclient.config;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: NettyConnectListion
 * @Description:
 * @Author: xue
 * @Date: 2022/7/27
 */

public class NettyConnectListion implements ChannelFutureListener {

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(!future.isSuccess()){
            final EventLoop loop = future.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        Client client = new Client("127.0.0.1",9001);
                        client.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },3, TimeUnit.SECONDS);
        }else {

            future.channel().writeAndFlush("给服务端发送消息"+UUID.randomUUID().toString());
            System.out.println("客户端连接服务端成功");
        }
    }
}
