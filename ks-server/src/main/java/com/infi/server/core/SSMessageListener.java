package com.infi.server.core;


import io.netty.channel.socket.DatagramPacket;

/**
 * @Author: kelvin
 * @Date: 2017-12-26
 * @Company:
 * @Description:
 */
public interface SSMessageListener {
    void onMessage(Object socketDescriptor, DatagramPacket packet);
}
