package com.infi.client.core;

import com.infi.client.AppClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

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
        LOG.info("ClientUDPHandler response is:{}", packet.content());
        AppClient.udpChannel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(packet.content()), new InetSocketAddress(AppClient.host, AppClient.serverUdpPort))).sync();
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
