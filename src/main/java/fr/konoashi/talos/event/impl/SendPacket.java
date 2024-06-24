package fr.konoashi.talos.event.impl;

import fr.konoashi.talos.event.Event;
import io.netty.buffer.ByteBuf;

public class SendPacket extends Event {
    private ByteBuf buffer;

    private String roomId;


    public ByteBuf getBuffer() {
        return buffer;
    }

    public String getRoomId() {
        return roomId;
    }


    public SendPacket(String roomId, ByteBuf buffer) {
        this.buffer = buffer;
        this.roomId = roomId;
    }


}
