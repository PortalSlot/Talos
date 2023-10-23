package fr.konoashi.talos.api;

import fr.konoashi.talos.ClientHandler;
import io.netty.buffer.Unpooled;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GoToServerWS extends TextWebSocketHandler {
    public static Map<WebSocketSession, ClientHandler> sessions = new HashMap<>();
    public static List<WebSocketSession> authorized = new ArrayList<>();
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException, ParseException {
        String request = message.getPayload();
        System.out.println(request);

        if (authorized.contains(session)) {
            sessions.get(session).getTcpClientSession().sendToServer(Unpooled.copiedBuffer(StandardCharsets.US_ASCII.encode(message.getPayload()).array()));
        }
        JSONObject requestJson = (JSONObject) new JSONParser().parse(request);

        if (ApiRest.sessions.get(request) != null) {
            sessions.put(session, ApiRest.sessions.get(requestJson.getString("botId")));
            authorized.add(session);
            session.sendMessage(new TextMessage("Success!"));
        } else {
            session.sendMessage(new TextMessage("Invalid session ID!"));
        }
    }
}
