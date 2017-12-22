package com.infi;

import org.junit.Test;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: kelvin
 * @Date: 2017-12-22
 * @Company:
 * @Description:
 */
public class PcapTest {

    @Test
    public void devTest() throws PcapNativeException, NotOpenException, InterruptedException {
        List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();
        allDevs.stream().forEach(x -> System.out.println(Arrays.toString(new List[]{x.getAddresses()})));

        // 抓取包长度
        int snaplen = 64 * 1024;
        // 超时50ms
        int timeout = 50;

        System.out.println("##########" +allDevs.get(2).getName());
        PcapNetworkInterface nif = Pcaps.getDevByName(allDevs.get(2).getName());
        PcapHandle.Builder phb =
                new PcapHandle.Builder(nif.getName()).snaplen(snaplen)
                .promiscuousMode(PcapNetworkInterface.PromiscuousMode.PROMISCUOUS)
                .timeoutMillis(timeout)
                .bufferSize(1 * 1024 * 1024);
        PcapHandle handle = phb.build();

        String filter = "ip and udp and (dst host 45.76.10.105)";
        handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        handle.loop(10, (PacketListener) packet -> {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println(packet.getPayload());
        });

    }
}
