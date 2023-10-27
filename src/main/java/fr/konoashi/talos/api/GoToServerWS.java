package fr.konoashi.talos.api;

import fr.konoashi.talos.ClientHandler;
import io.netty.buffer.Unpooled;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GoToServerWS extends TextWebSocketHandler {
    public static Map<WebSocketSession, ClientHandler> sessions = new HashMap<>();
    public static List<WebSocketSession> authorized = new ArrayList<>();
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException, ParseException {
        String request = message.getPayload();
        System.out.println(request);

        JSONObject requestJson = (org.json.simple.JSONObject) new JSONParser().parse(request);

        if (ApiRest.sessions.get(requestJson.get("botId").toString()) != null) {
            sessions.put(session, ApiRest.sessions.get(requestJson.get("botId").toString()));
            authorized.add(session);
            session.sendMessage(new TextMessage("Success!"));
        } else {
            System.out.println("Invalid session ID!");
            session.sendMessage(new TextMessage("Invalid session ID!"));
        }
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        ByteBuffer request = message.getPayload();

        if (authorized.contains(session)) {
            sessions.get(session).getTcpClientSession().sendToServer(Unpooled.copiedBuffer(request));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        authorized.remove(session);
    }
}
