package fr.konoashi.talos.event.impl;

import fr.konoashi.talos.TcpClientSession;
import fr.konoashi.talos.event.Event;

public class ConnEstablishedC2S extends Event {

    TcpClientSession client;

    public ConnEstablishedC2S(TcpClientSession client) {
        this.client = client;
    }

    public TcpClientSession getClient() {
        return client;
    }
}
