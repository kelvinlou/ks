package com.infi.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

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


    //谚语列表
    private static final String[] DICTIONARY = { "只要功夫深，铁棒磨成针。",
            "旧时王谢堂前燕,飞入寻常百姓家。", "洛阳亲友如相问，一片冰心在玉壶。", "一寸光阴一寸金，寸金难买寸光阴。",
            "老骥伏枥，志在千里，烈士暮年，壮心不已" };
    private String nextQuote(){
        //返回0-DICTIONARY.length中的一个整数。
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];//将谚语列表中对应的谚语返回
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String content = packet.content().toString(CharsetUtil.UTF_8);
        LOG.info("server receive packet:{}, sender is:{}", content, packet.sender());

        ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语查询结果："+nextQuote(),CharsetUtil.UTF_8), packet.sender()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("netty ServerUDPHandler error:{}", cause);
        ctx.close();
    }
}
