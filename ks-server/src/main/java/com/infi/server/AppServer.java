package com.infi.server;


import com.infi.common.contants.ServerCONT;
import com.infi.server.core.ServerUDPHandler;
import com.infi.server.core.TcpServerHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


@SpringBootApplication
public class AppServer
{
    private final static Logger LOG = LoggerFactory.getLogger(AppServer.class);

    public static Channel tcpChannel;
    public static Channel udpChannel;
    public static String host = "127.0.0.1";
    public static int udpPort = 150;
    public static int tcpPort = 8388;

    public static void main( String[] args )
    {
        LOG.info("#####start#####" + ServerCONT.IP);
        SpringApplication.run(AppServer.class, args);

        AppServer app = new AppServer();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(()-> app.runUDP(host, udpPort));
        executorService.submit(()-> app.runTCP(host, tcpPort));
        LOG.info("#####end#####");
    }



    private void runUDP(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ServerUDPHandler());
            udpChannel = b.bind(port).sync().channel();
            udpChannel.closeFuture().await();
        } catch (InterruptedException e) {
            LOG.error("netty server interrupt: {}", e);
        } finally {
            group.shutdownGracefully();
        }
    }

    private void runTCP(String host, int port) {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group);
        b.channel(NioSocketChannel.class)
         .option(ChannelOption.TCP_NODELAY, true)
         .handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel ch) throws Exception {
                 ChannelPipeline pipeline = ch.pipeline();
                 pipeline.addLast(new TcpServerHandler());
             }
         });
        try {
            tcpChannel = b.connect(host, port).sync().channel();
            tcpChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("TCP服务器启动失败:{}", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
