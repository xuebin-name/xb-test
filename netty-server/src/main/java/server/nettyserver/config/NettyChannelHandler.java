package server.nettyserver.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class NettyChannelHandler extends ChannelDuplexHandler {

    private Logger logger = LoggerFactory.getLogger(NettyChannelHandler.class);



    //处理硬件发送的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String str = "服务端收到你的消息了";
        logger.info("服务端收到消息为"+buf.toString(CharsetUtil.UTF_8));
        ctx.write(str.getBytes("UTF-8"));
        ctx.flush();
    }

    //向硬件发送数据
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.write(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        switch (event.state()){
            case ALL_IDLE:
                logger.info("读写空闲");
            case READER_IDLE:
                logger.info("读空闲");
            case WRITER_IDLE:
                logger.info("写空闲");
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("建立链接成功---》"+ctx.channel());
    }



}
