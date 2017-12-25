package com.infi.server.core;

import org.pcap4j.packet.Packet;

/**
 * @Author: kelvin
 * @Date: 2017-12-25
 * @Company:
 * @Description:
 */
public interface SendService {

    void send(Packet packet);

}
