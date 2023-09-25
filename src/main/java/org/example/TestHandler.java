package org.example;

import org.example.event.SubscribeEvent;
import org.example.event.impl.ReceivePacket;

import java.io.IOException;
import java.util.Arrays;

public class TestHandler {
    @SubscribeEvent
    public void receivedPacket(ReceivePacket e) throws IOException {
        System.out.println("Received: " + Arrays.toString(e.getBuffer()));
    }
}
