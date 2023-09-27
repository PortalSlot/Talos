package fr.konoashi.talos.event.impl;


import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import fr.konoashi.talos.event.Event;

public class ReceivePacket extends Event {
    private ByteBuf buffer;

    private String username;

    private String ip;

    private Channel channel;

    private int id;

    public ByteBuf getBuffer() {
        return buffer;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public Channel getChannel() {
        return channel;
    }

    public int getId() {
        return id;
    }

    public ReceivePacket(String username, String ip, Channel channel, ByteBuf buffer, int id) {
        this.buffer = buffer;
        this.username = username;
        this.ip = ip;
        this.channel = channel;
        this.id = id;
    }

}
