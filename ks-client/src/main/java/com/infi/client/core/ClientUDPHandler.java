package com.infi.client.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ClientUDPHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final Logger LOG = LoggerFactory.getLogger(ClientUDPHandler.class);

    private ChannelHandlerContext tcpChannel;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String response = packet.content().toString(CharsetUtil.UTF_8);
        LOG.info("response is:{}", response);
//        tcpChannel.writeAndFlush(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("netty ClientUDPHandler error:{}", cause);
        ctx.close();
    }

    public void setTcpChannel(ChannelHandlerContext tcpChannel) {
        this.tcpChannel = tcpChannel;
    }
}
