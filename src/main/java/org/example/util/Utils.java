package org.example.util;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void joinServer(String profileUuid, String authenticationToken, String serverId) throws IOException {

        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/join");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);
        String outString = "{\"accessToken\":\"" + authenticationToken + "\", \n" +
                " \"selectedProfile\":\"" + profileUuid + "\", \n" +
                " \"serverId\":\"" + serverId + "\"\n" +
                "}";
        System.out.println(outString);
        byte[] out = outString.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
    }
}
