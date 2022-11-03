package server.nettyserver.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class NettyConfig {

    private Logger logger = LoggerFactory.getLogger(NettyConfig.class);

    public static ThreadPoolExecutor poolExecutor;

    public static Selector selector;

    static {
        poolExecutor = new ThreadPoolExecutor(8, 16, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<>(32),(r,executor)->{
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    executor.toString());
        });
    }


    @Value("${netty.server.port}")
    private int port=9001;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void multiplexing(){
        ServerSocketChannel serverChannel=null;

        try {
            selector = Selector.open();

            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("启动完成");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //启动服务端netty 服务
    public void startServer(){
        logger.info("netty server -----------> start!");
        EventLoopGroup bossGroup = new NioEventLoopGroup(0,poolExecutor); //主线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup(0,poolExecutor);//工作线程组
        logger.info("connect is already ---------->start!");
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            logger.info("开始初始化");
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))

                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            //添加分隔符解码器
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1024*2, Unpooled.copiedBuffer("\r\n\r\n".getBytes())));
                            //心跳检测
                            pipeline.addLast(new IdleStateHandler(2,2,2, TimeUnit.SECONDS));
                            pipeline.addLast(new NettyChannelHandler());

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture future = bootstrap.bind(port);
            //添加一个监听回调
            future.addListener((ChannelFutureListener) channelFuture -> {
                if(channelFuture.isSuccess()){
                    logger.info("服务绑定");
                }else {
                    logger.info("服务绑定失败");
                }
            });
            //future.channel().closeFuture();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}

