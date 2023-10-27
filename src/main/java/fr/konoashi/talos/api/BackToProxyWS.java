package fr.konoashi.talos.api;

import fr.konoashi.talos.ClientHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BackToProxyWS extends TextWebSocketHandler {

    public static Map<ClientHandler, WebSocketSession> sessions = new HashMap<>();
    public static Map<WebSocketSession, ClientHandler> sessionsBackward = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String request = message.getPayload();
        System.out.println(request);

        if (ApiRest.sessions.get(request) != null) {
            sessions.put(ApiRest.sessions.get(request), session);
            sessionsBackward.put(session, ApiRest.sessions.get(request));
            session.sendMessage(new TextMessage("Success!"));
        } else {
            System.out.println("Invalid session ID!");
            session.sendMessage(new TextMessage("Invalid bot ID!"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String botId = sessionsBackward.get(session).getBotId();
        sessionsBackward.get(sessions.get(ApiRest.sessions.get(botId))).getTcpClientSession().disconnect();
        sessionsBackward.remove(session);
        sessions.remove(ApiRest.sessions.get(botId));
        ApiRest.sessions.remove(botId);
    }
    //Il faut envoyer depuis l'event face les packets a toutes les sessions websocket -> donc on envoie tout les packets de toutes les personnes connectés à un talos précis qui a été open par une requete POST a cette api

}
