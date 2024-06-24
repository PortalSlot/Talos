package fr.konoashi.talos;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import fr.konoashi.talos.network.ProtocolState;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientHandler {
    private TcpClientSession tcpClientSession;
    private String username;

    private String botId;

    public ClientHandler(String botId, String username, String uuid, String ssid, int protocolVersion) {
        this.botId = botId;
        this.tcpClientSession = new TcpClientSession(username, uuid, ssid, protocolVersion, this);
        this.username = username;
    }

    public void connectToServer(String ip, int port) {
        //TODO: Fix this horrible thing
        tcpClientSession.connect3(ip, port);
        /*ByteBuf bufHandshake = Unpooled.buffer(16);
        PacketBuffer packetHandshake = new PacketBuffer(bufHandshake);
        packetHandshake.writeByte(0);
        packetHandshake.writeVarIntToBuffer(47);
        packetHandshake.writeString(ip);
        packetHandshake.writeVarIntToBuffer(port);
        packetHandshake.writeVarIntToBuffer(2);
        System.out.println(Arrays.toString(bufHandshake.array()));
        tcpClientSession.sendToServer(bufHandshake);

        tcpClientSession.setState(ProtocolState.LOGIN);

        ByteBuf bufLoginStart = Unpooled.buffer(10);
        PacketBuffer packetLoginStart = new PacketBuffer(bufLoginStart);
        packetLoginStart.writeByte(0);
        packetLoginStart.writeString(username);
        System.out.println(Arrays.toString(bufLoginStart.array()));
        tcpClientSession.sendToServer(bufLoginStart);*/
        char[] ch = this.username.toCharArray(); //it will read and store each character of String and store into char[].
        ByteBuf pUB = Unpooled.buffer();
        pUB.capacity(this.username.length()+2);
        pUB.writeBytes(new byte[]{0, (byte) this.username.length()});
        pUB.writeBytes(Unpooled.copiedBuffer(ch, StandardCharsets.US_ASCII).array());
        System.out.println(tcpClientSession.futureClientChannel.remoteAddress().toString());
        System.out.println(tcpClientSession.futureClientChannel.isOpen());
        tcpClientSession.sendToServer(Unpooled.copiedBuffer(new byte[]{0, 47, 9, 108, 111, 99, 97, 108, 104, 111, 115, 116, 99, -37, 2}));
        tcpClientSession.setState(ProtocolState.LOGIN);
        tcpClientSession.sendToServer(pUB);
    }

    public TcpClientSession getTcpClientSession() {
        return tcpClientSession;
    }

    public String getUsername() {
        return username;
    }

    public String getBotId() {
        return botId;
    }
}
