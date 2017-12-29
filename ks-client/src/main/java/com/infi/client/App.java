package com.infi.client;

import com.infi.client.core.ClientUDPHandler;
import com.infi.client.core.TcpServerHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;


@SpringBootApplication
public class App {
    private final static Logger LOG = LoggerFactory.getLogger(App.class);
    /** 用于分配处理业务线程的线程组个数 */
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2; // 默认
    /** 业务出现线程大小 */
    protected static final int BIZTHREADSIZE = 4;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

    public static Channel tcpChannel;
    public static Channel udpChannel;

    public static void main(String[] args) {
        int udpPort = 8780;
        int tcpPort = 8091;
        SpringApplication.run(App.class, args);
        App app = new App();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.submit(()-> app.runUDP(udpPort));
        executorService.submit(()-> app.runTCP(tcpPort));

    }

    private void runUDP(int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ClientUDPHandler());

            udpChannel = b.bind(port).sync().channel();
            udpChannel.closeFuture().await();

        } catch (InterruptedException e) {
            LOG.error("netty client interrupted:{}", e);
        } finally {
            group.shutdownGracefully();
        }

    }


    private void runTCP(int port) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new TcpServerHandler());
            }
        });

        try {
            tcpChannel = b.bind(port).sync().channel();
        } catch (InterruptedException e) {
            LOG.error("TCP服务器启动失败:{}", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

        LOG.info("TCP服务器已启动");
    }

}
