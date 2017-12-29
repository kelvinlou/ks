package com.infi.client.core;

import com.infi.client.App;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author: kelvin
 * @Date: 2017-12-26
 * @Company:
 * @Description:
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(TcpServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info("receive msg:{}", msg);
//        ByteBuf buffer = ctx.alloc().buffer();
//        buffer.writeBytes("111".getBytes());
//        ctx.writeAndFlush(buffer);
        ByteBuf byteBuf = Unpooled.copiedBuffer((ByteBuf) msg);
        App.udpChannel.writeAndFlush(byteBuf);
//        udpChannel.channel().writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("netty error:{}", cause);
        ctx.close();
    }

//    public void setUdpChannel(ChannelHandlerContext udpChannel) {
//        this.udpChannel = udpChannel;
//    }
}
