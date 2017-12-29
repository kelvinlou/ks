package com.infi.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;


/**
 * @Author: kelvin
 * @Date: 2017-12-26
 * @Company:
 * @Description:
 */
public class SSProxyImpl implements SSProxy<DatagramPacket>{

    private static final Logger LOG = LoggerFactory.getLogger(SSProxyImpl.class);

    private Channel channel;

    @Override
    public void init() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, false)
                    .handler(new SSUDPHandler());
            channel = b.bind().sync().channel();

        } catch (InterruptedException e) {
            LOG.error("netty client interrupted:{}", e);
        } finally {
            group.shutdownGracefully();
        }

    }

    @Override
    public void send(DatagramPacket packet) {
        channel.writeAndFlush(packet);
    }

    @Override
    public DatagramPacket receive() {
        return null;
    }
}
