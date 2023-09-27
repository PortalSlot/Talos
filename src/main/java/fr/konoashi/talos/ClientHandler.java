package fr.konoashi.talos;

import io.netty.buffer.Unpooled;
import fr.konoashi.talos.network.ProtocolState;

public class ClientHandler {
    TcpClientSession tcpClientSession;
    String username;

    public ClientHandler(String username, String uuid, String ssid) {
        this.tcpClientSession = new TcpClientSession(username, uuid, ssid);
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
        tcpClientSession.sendToServer(Unpooled.copiedBuffer(new byte[]{0, 47, 9, 108, 111, 99, 97, 108, 104, 111, 115, 116, 99, -37, 2}));
        tcpClientSession.setState(ProtocolState.LOGIN);
        tcpClientSession.sendToServer(Unpooled.copiedBuffer(new byte[]{0, 8, 107, 111, 110, 111, 97, 115, 104, 105}));
    }

    public TcpClientSession getTcpClientSession() {
        return tcpClientSession;
    }

    public String getUsername() {
        return username;
    }

}
