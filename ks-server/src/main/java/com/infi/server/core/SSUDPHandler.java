package com.infi.server.core;

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
public class SSUDPHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger LOG = LoggerFactory.getLogger(SSUDPHandler.class);

    private SSMessageListener ssMessageListener;

    public void setListener(SSMessageListener listener) {
        this.ssMessageListener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String content = packet.content().toString(CharsetUtil.UTF_8);
        LOG.info("SS receive content:{}", content);
        ssMessageListener.onMessage(ctx, packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOG.error("netty error:{}", cause);
    }
}
