package fr.konoashi.talos.api;

import fr.konoashi.talos.api.stomp.MessageSender;
import fr.konoashi.talos.event.SubscribeEvent;
import fr.konoashi.talos.event.impl.ConnEstablishedC2S;
import fr.konoashi.talos.event.impl.PlayerDisconnect;
import fr.konoashi.talos.event.impl.ReceivePacket;
import fr.konoashi.talos.event.impl.SendPacket;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class Event {

    @SubscribeEvent
    public static void event(ConnEstablishedC2S e) throws IOException {
        System.out.println("Established conn between client and server");
    }

    @SubscribeEvent
    public static void event(PlayerDisconnect e) throws IOException {
        String roomId = TalosApiController.talosIdTalosRoomIdSetMap.get(e.getClientSession().getClientHandler().getBotId());
        TalosApiController.talosIdTalosRoomIdSetMap.remove(e.getClientSession().getClientHandler().getBotId());
        TalosApiController.idTalosMap.remove(e.getClientSession().getClientHandler().getBotId());
        //new WebsocketMessageSender(RoomController.messagingTemplate).
    }

    @SubscribeEvent
    public static void event(ReceivePacket e) throws IOException {

        //System.out.println("Bot id: " + e.getClientSession().getClientHandler().getBotId());
        String roomId = TalosApiController.talosIdTalosRoomIdSetMap.get(e.getClientSession().getClientHandler().getBotId());
        //System.out.println("send to websocket room: " + roomId);

        MessageSender.sendMessage("/room/" + roomId, e.getBuffer().array());
        //System.out.println("fjhbefhvbefhvbe" + Arrays.toString(e.getBuffer().array()));
        //idk why it is not sending the message to the websocket help


    }

    @SubscribeEvent
    public static void event(SendPacket e) throws IOException {

         AtomicReference<String> talosId = new AtomicReference<>();
         TalosApiController.talosIdTalosRoomIdSetMap.forEach((key, value) -> {
            if (value.contains(e.getRoomId())) {
                talosId.set(key);
            }
         });
         TalosApiController.idTalosMap.get(talosId.get()).getTcpClientSession().sendToServer(Unpooled.copiedBuffer(e.getBuffer()));

    }
}
