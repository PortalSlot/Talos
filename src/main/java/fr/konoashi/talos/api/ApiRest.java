package fr.konoashi.talos.api;

import com.google.gson.JsonArray;
import com.grack.nanojson.JsonObject;
import fr.konoashi.talos.ClientHandler;
import fr.konoashi.talos.event.Event;
import fr.konoashi.talos.event.EventManager;
import org.json.simple.JSONObject;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class ApiRest {

    public static Map<String, ClientHandler> sessions = new HashMap<>();

    @PostMapping(value = "/startBot")
    public String startProxyServer(@RequestBody JSONObject rawdata) throws IOException {
        System.out.println("Starting bot...");
        System.out.println(rawdata);

        ClientHandler client = new ClientHandler(rawdata.get("username").toString(), rawdata.get("uuid").toString(), rawdata.get("ssid").toString(), Integer.parseInt(rawdata.get("protocol").toString()));

        String id = rawdata.get("botId").toString();
        sessions.put(id, client);
        return id;
    }

    @PostMapping(value = "/joinServer")
    public String connectToServer(@RequestBody JSONObject rawdata) throws IOException {
        System.out.println("Joining server...");

        String id = rawdata.get("botId").toString();
        ClientHandler client = sessions.get(id);

        //Start a websocket and send the code to access this Face instancegv
        EventManager.register(new Event());
        client.connectToServer(rawdata.get("ip").toString(), Integer.parseInt(rawdata.get("port").toString()));

        if (client.getTcpClientSession() == null) {
            return null;
        } else {
            return client.getTcpClientSession().getState().toString();
        }

    }

}

