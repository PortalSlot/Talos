package org.example;

import org.example.event.EventManager;
import org.example.event.SubscribeEvent;
import org.example.event.impl.ReceivePacket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        ClientHandler ok = new ClientHandler("konoashi", "8aad957a70e84a8f94eace1a75422525", "eyJraWQiOiJhYzg0YSIsImFsZyI6IkhTMjU2In0.eyJ4dWlkIjoiMjUzNTQ3Mzc1Njg4NDIwNCIsImFnZyI6IkFkdWx0Iiwic3ViIjoiMGFmOWVlNjEtM2ZkNy00NDNjLThhMmYtNGNmNTQ1YzdhNTUxIiwiYXV0aCI6IlhCT1giLCJucyI6ImRlZmF1bHQiLCJyb2xlcyI6W10sImlzcyI6ImF1dGhlbnRpY2F0aW9uIiwiZmxhZ3MiOlsidHdvZmFjdG9yYXV0aCIsIm1pbmVjcmFmdF9uZXQiLCJtc2FtaWdyYXRpb25fc3RhZ2U0Iiwib3JkZXJzXzIwMjIiXSwicHJvZmlsZXMiOnsibWMiOiI4YWFkOTU3YS03MGU4LTRhOGYtOTRlYS1jZTFhNzU0MjI1MjUifSwicGxhdGZvcm0iOiJVTktOT1dOIiwieXVpZCI6ImQwNWE4ZTIxOTBlZGU5OTk3ODUwMzcwZDIzMjJhZTQxIiwibmJmIjoxNjk1NjQ2MTE3LCJleHAiOjE2OTU3MzI1MTcsImlhdCI6MTY5NTY0NjExN30.v9ET30pxOL3LeF37TH98sr-5LfiAMfHWsT9WTRIU9K0");
        ok.connectToServer("localhost", 25563);
        EventManager.register(new TestHandler());
    }


}
