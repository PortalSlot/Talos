package fr.konoashi.talos.api;

import fr.konoashi.talos.event.SubscribeEvent;
import fr.konoashi.talos.event.impl.ConnEstablishedC2S;
import fr.konoashi.talos.event.impl.ReceivePacket;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Arrays;

public class Event {
    @SubscribeEvent
    public static void event(ConnEstablishedC2S e) throws IOException {
        System.out.println("Established conn between client and proxy");
        BackToProxyWS.sessions.get(e.getClient().getClientHandler()).sendMessage(new TextMessage("Established connexion between Client and Server"));
    }

    @SubscribeEvent
    public static void event(ReceivePacket e) throws IOException {
        BackToProxyWS.sessions.get(e.getClientSession().getClientHandler()).sendMessage(new BinaryMessage(e.getBuffer().array()));
        System.out.println("Send to client: " +  Arrays.toString(e.getBuffer().array()));
    }
}
