package fr.konoashi.talos.event.impl;

import fr.konoashi.talos.TcpClientSession;
import fr.konoashi.talos.event.Event;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class PlayerDisconnect extends Event {

    public TcpClientSession clientSession;


    public TcpClientSession getClientSession() {
        return clientSession;
    }

    public PlayerDisconnect(TcpClientSession clientSession) {

        this.clientSession = clientSession;
    }

}
