package org.example;


import io.netty.buffer.Unpooled;
import org.example.network.ProtocolState;

import java.net.Proxy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;

public class Main {

    public static void main(String[] args) {
        TcpClientSession client = new TcpClientSession();
        client.connect3();
        client.sendToServer(Unpooled.copiedBuffer(new byte[]{0, 47, 9, 108, 111, 99, 97, 108, 104, 111, 115, 116, 99, -37, 2}));
        client.setState(ProtocolState.LOGIN);
        client.sendToServer(Unpooled.copiedBuffer(new byte[]{0, 8, 107, 111, 110, 111, 97, 115, 104, 105}));
    }
}