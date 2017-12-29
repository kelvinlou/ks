package com.infi.server;


import com.infi.common.contants.ServerCONT;
import com.infi.server.core.ServerUDPHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;


@SpringBootApplication
public class App
{
    private final static Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        LOG.info("#####start#####" + ServerCONT.IP);
        SpringApplication.run(App.class, args);
        int port = 150;
        new App().run(port);
        LOG.info("#####end#####");
    }



    private void run(int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ServerUDPHandler());
            b.bind(port).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            LOG.error("netty server interrupt: {}", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
