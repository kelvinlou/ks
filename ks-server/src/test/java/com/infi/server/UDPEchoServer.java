package com.infi.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPEchoServer {

    private static final int ECHOMAX = 255; // 发送或接收的信息最大字节数

    public static void main(String[] args) throws IOException {

        if (args.length != 1) { // Test for correct argument list
            throw new IllegalArgumentException("Parameter(s): <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        DatagramSocket socket = new DatagramSocket(servPort);
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

        while (true) { // 不断接收来自客户端的信息及作出相应的相应
            socket.receive(packet); // Receive packet from client
            System.out.println("Handling client at " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());
            socket.send(packet); // 将客户端发送来的信息返回给客户端
            packet.setLength(ECHOMAX);
            // 重置packet的内部长度，因为处理了接收到的信息后，数据包的内部长度将被
            //设置为刚处理过的信息的长度，而这个长度可能比缓冲区的原始长度还要短，
            //如果不重置，而且接收到的新信息长于这个内部长度，则超出长度的部分将会被截断，所以这点必须注意到。
        }
        /* NOT REACHED */
    }
}