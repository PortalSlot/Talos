package fr.konoashi.talos.api.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.konoashi.talos.ClientHandler;
import fr.konoashi.talos.api.Event;
import fr.konoashi.talos.api.TalosApiController;
import fr.konoashi.talos.event.EventManager;
import org.json.simple.JSONObject;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping
public class ApiRest {

    @PostMapping(value = "/startBot")
    public String startProxyServer(@RequestBody JSONObject rawdata) throws IOException {
        System.out.println("Starting bot...");
        System.out.println(rawdata);
        ClientHandler client = new ClientHandler(rawdata.get("botId").toString() ,rawdata.get("username").toString(), rawdata.get("uuid").toString(), rawdata.get("ssid").toString(), Integer.parseInt(rawdata.get("protocol").toString()));
        String id = rawdata.get("botId").toString();
        TalosApiController.idTalosMap.put(id, client);

        String roomId = UUID.randomUUID().toString();
        TalosApiController.talosIdTalosRoomIdSetMap.put(id, roomId);
        System.out.println("Room id: " + roomId + " for bot id: " + id);

        return roomId;
    }

    @PostMapping(value = "/joinServer")
    public String connectToServer(@RequestBody JSONObject rawdata) throws IOException {
        System.out.println("Joining server...");

        String id = rawdata.get("botId").toString();
        ClientHandler client = TalosApiController.idTalosMap.get(id);

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

