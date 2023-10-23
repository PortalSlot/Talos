package fr.konoashi.talos.api;

import fr.konoashi.talos.ClientHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BackToProxyWS extends TextWebSocketHandler {

    public static Map<ClientHandler, WebSocketSession> sessions = new HashMap<>();
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String request = message.getPayload();
        System.out.println(request);

        if (ApiRest.sessions.get(request) != null) {
            sessions.put(ApiRest.sessions.get(request), session);
            session.sendMessage(new TextMessage("Success!"));
        } else {
            session.sendMessage(new TextMessage("Invalid bot ID!"));
        }
    }
    //Il faut envoyer depuis l'event face les packets a toutes les sessions websocket -> donc on envoie tout les packets de toutes les personnes connectés à un talos précis qui a été open par une requete POST a cette api

}
