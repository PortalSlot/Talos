package org.example.event.impl;


import io.netty.channel.Channel;
import org.example.event.Event;

public class ReceivePacket extends Event {
    private byte[] buffer;

    private String username;

    private String ip;

    private Channel channel;

    private int id;

    public byte[] getBuffer() {
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

    public ReceivePacket(String username, String ip, Channel channel, byte[] buffer, int id) {
        this.buffer = buffer;
        this.username = username;
        this.ip = ip;
        this.channel = channel;
        this.id = id;
    }

}
