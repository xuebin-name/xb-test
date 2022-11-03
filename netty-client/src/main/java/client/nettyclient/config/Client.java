package client.nettyclient.config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class Client {
    private final int port;

    private final String host;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        String hostname = "127.0.0.1";
        int port = 9001;
        new Client(hostname,port).start();
    }

    public void start() throws InterruptedException {
        //事件组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //客户端辅助启动类
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class) //设置管道类型
                    .remoteAddress(new InetSocketAddress(host, port)) //设置连接地址及端口
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception { //设置处理器  业务处理
                            //channel.pipeline().addLast(new ClientHandler());

                            channel.pipeline().addLast(new ClientInlize());
                            channel.pipeline().addLast(new HertBeatHandler());
                        }
                    });
            ChannelFuture future = b.connect().sync();
            future.addListener(new NettyConnectListion());
            future.channel().writeAndFlush("客户端请求连接服务端----");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }

    }
}
