package fr.konoashi.talos.pipeline;

import fr.konoashi.talos.TcpClientSession;
import fr.konoashi.talos.event.impl.ConnEstablishedC2S;
import fr.konoashi.talos.util.AuthUtils;
import fr.konoashi.talos.util.CryptUtil;
import fr.konoashi.talos.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import fr.konoashi.talos.event.impl.ReceivePacket;
import fr.konoashi.talos.network.PacketBuffer;
import fr.konoashi.talos.network.ProtocolState;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Arrays;

public class NetworkPacketHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private TcpClientSession client;

    public NetworkPacketHandler(TcpClientSession client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //Act as a server for the real client
        byte[] bytes = new byte[msg.readableBytes()];
        msg.duplicate().readBytes(bytes);
        ByteBuf copiedBuffer = Unpooled.copiedBuffer(bytes);
        PacketBuffer packetBuffer = new PacketBuffer(copiedBuffer);
        System.out.println("Should be sent to client: " + Arrays.toString(copiedBuffer.array()));

        int packetId = packetBuffer.readVarIntFromBuffer();

            if (this.client.getState() == ProtocolState.STATUS) {
                if (packetId == 0x00) {
                    this.client.sendToServer(msg);
                }
                if (packetId == 0x01) {
                    this.client.sendToServer(msg);
                    this.client.disconnect();
                }
            } else if (this.client.getState() == ProtocolState.LOGIN) {
                if (packetId == 0x00) {
                    this.client.disconnect(); //that's the disconnect packet
                }
                if (packetId == 0x01) {
                    //There we receive the encryption start packet and we need to send the encryption response and join session on mojang api
                    String serverId = packetBuffer.readStringFromBuffer(18);
                    byte[] publicKeyBytes = packetBuffer.readByteArray();
                    PublicKey publicKey = CryptUtil.readPublicKey(publicKeyBytes);
                    byte[] nonce = packetBuffer.readByteArray();
                    SecretKey sharedSecret = CryptUtil.generateSharedSecret();
                    String serverHash = AuthUtils.calculateServerHash(serverId, publicKey, sharedSecret);

                    Utils.joinServer(this.client.getUuid(), this.client.getSsid(), serverHash);
                    encryptionResponse(publicKey, nonce, sharedSecret);
                    this.client.enableEncryption(sharedSecret);
                }
                if (packetId == 0x02) {
                    new ConnEstablishedC2S(client).call();
                    System.out.println("Login success: " + client.clientChannel);
                    this.client.setState(ProtocolState.PLAY);
                }
                if (packetId == 0x03) {
                    this.client.setCompressionThreshold(packetBuffer.readVarIntFromBuffer());
                }
            } else if (this.client.getState() == ProtocolState.PLAY) {
                if (packetId == 0x00) {
                    keepAlive(packetBuffer.readVarIntFromBuffer());
                } else if (packetId == 64) {
                    new ReceivePacket(this.client.getUsername(), this.client.clientChannel.remoteAddress().toString(), this.client.clientChannel, copiedBuffer, packetId, this.client).call();
                    this.client.disconnect();
                }
                else {
                    new ReceivePacket(this.client.getUsername(), this.client.clientChannel.remoteAddress().toString(), this.client.clientChannel, copiedBuffer, packetId, this.client).call();
                }
            }


    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.client.disconnect();
    }

    public void encryptionResponse(PublicKey publicKey, byte[] nonce, SecretKey sharedSecret) {
        ByteBuf buf = Unpooled.buffer();
        PacketBuffer packet = new PacketBuffer(buf);

        packet.writeVarIntToBuffer(1);
        packet.writeByteArray(CryptUtil.encrypt(sharedSecret.getEncoded(), publicKey));
        packet.writeByteArray(CryptUtil.encrypt(nonce, publicKey));

        this.client.sendToServer(Unpooled.copiedBuffer(Arrays.copyOfRange(packet.array(), 0, 261)));
    }

    public void keepAlive(int varInt) {
        ByteBuf buf = Unpooled.buffer();
        PacketBuffer packet = new PacketBuffer(buf);

        packet.writeVarIntToBuffer(0);
        packet.writeVarIntToBuffer(varInt);

        this.client.sendToServer(Unpooled.copiedBuffer(Arrays.copyOfRange(packet.array(), 0, 5)));
    }

    public void respawn() {
        ByteBuf buf = Unpooled.buffer();
        PacketBuffer packet = new PacketBuffer(buf);

        packet.writeByte(22);
        packet.writeVarIntToBuffer(0);

        this.client.sendToServer(Unpooled.copiedBuffer(Arrays.copyOfRange(packet.array(), 0, 2)));
    }

}
