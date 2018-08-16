package com.infi.server.core;

import com.infi.server.AppServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * @Author: kelvin
 * @Date: 2017-12-26
 * @Company:
 * @Description:
 */
public class ServerUDPHandler extends SimpleChannelInboundHandler<DatagramPacket>{
    private final static Logger LOG = LoggerFactory.getLogger(ServerUDPHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        LOG.info("ServerUDPHandler receive packet:{}, sender is:{}", packet, packet.sender());
//        ByteBuf buffer = AppServer.tcpChannel.alloc().buffer();
//        buffer.writeBytes(packet.content());
        AppServer.tcpChannel.writeAndFlush(Unpooled.copiedBuffer(packet.content()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("netty ServerUDPHandler error:{}", cause);
        ctx.close();
    }
}
