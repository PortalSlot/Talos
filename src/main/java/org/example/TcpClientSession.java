package org.example;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.network.LazyLoadBase;
import org.example.network.ProtocolState;
import org.example.pipeline.*;
import org.tinylog.Logger;

import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.UUID;

public class TcpClientSession {

    public Channel clientChannel;

    public Channel futureClientChannel;

    private ProtocolState state = ProtocolState.HANDSHAKING;
    private int compressionThreshold = -1;

    private String username;
    private String uuid;
    private String ssid;

    public TcpClientSession(String username, String uuid, String ssid) {
        this.username = username;
        this.uuid = uuid;
        this.ssid = ssid;
    }

    public void connect3(String ip, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class).remoteAddress(ip, port)
                .option(ChannelOption.IP_TOS, 0x18)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_RCVBUF, 1024 * 1024).option(ChannelOption.SO_SNDBUF, 1024 * 1024).option(ChannelOption.SO_REUSEADDR, true);


        Bootstrap finalBootstrap = bootstrap;
        bootstrap = bootstrap.handler(new ChannelInitializer<SocketChannel>(){

            @Override
            protected void initChannel(SocketChannel channel){
                // for every channel create a custom UUID, cache the UUID and whenever we send responses well attach to that uuid, if it fails we know the response was not sent.
                // 50ms reconnection delay.
                clientChannel = channel;
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("sizer", new NetworkPacketSizer());
                //pipeline.addLast("codec", new NetworkPacketCodec());
                pipeline.addLast("handler", new NetworkPacketHandler(TcpClientSession.this));
            }
        });
        ChannelFuture channelFuture;
        try {
            channelFuture = bootstrap.connect("localhost", 25563).await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.futureClientChannel = channelFuture.channel();
    }



    public void disconnect() {
        this.disconnectClient();
    }

    public void disconnectClient() {
        if(clientChannel != null) {
            try {
                clientChannel.close().sync();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void closeChannel(Channel channel) {
        if(channel == null)
            return;

        try {
            channel.close().sync();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void setCompressionThreshold(int compressionThreshold) {
        Logger.info("Compression threshold set to {}", compressionThreshold);
        this.compressionThreshold = compressionThreshold;

        // At the moment, compression is only enabled for transport between the server and proxy.
        // There is no compression between the proxy and client.
        if(compressionThreshold >= 0) {
            this.addCompression(clientChannel);
        } else {
            this.removeCompression(clientChannel);
        }
    }

    private static final String COMPRESSION_HANDLER_NAME = "compression";
    private static final String ENCRYPTION_HANDLER_NAME = "encryption";

    private void addCompression(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        if(pipeline.get(COMPRESSION_HANDLER_NAME) == null)
            pipeline.addBefore("handler", COMPRESSION_HANDLER_NAME, new NetworkCompression(this));
        System.out.println(pipeline.names());
    }

    private void removeCompression(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        if(pipeline.get(COMPRESSION_HANDLER_NAME) != null)
            pipeline.remove(COMPRESSION_HANDLER_NAME);
    }

    public void enableEncryption(SecretKey sharedSecret) {
        try {
            clientChannel.pipeline().addBefore("sizer", ENCRYPTION_HANDLER_NAME, new NetworkEncryption(sharedSecret));
            Logger.info("Enabled encryption");
        } catch (GeneralSecurityException ex) {
            Logger.error(ex, "Failed to enable encryption");
        }
    }

    public void sendToServer(ByteBuf packet) {
        if(clientChannel.isWritable()) {
            try {
                System.out.println("Sent: " + Arrays.toString(packet.array()));
                clientChannel.writeAndFlush(packet).sync();
            } catch (Exception err){
                err.printStackTrace();
            }
        }
    }

    /*public void setUsername(String username) {
        this.username = username;
        this.account = AuthenticationHandler.getInstance().getByUsername(username);
    }*/

    public Channel getClientChannel() {
        return clientChannel;
    }

    public ProtocolState getState() {
        return state;
    }

    public int getCompressionThreshold() {
        return compressionThreshold;
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    public String getSsid() {
        return ssid;
    }

    /*public Account getAccount() {
        return account;
    }*/

    public void setState(ProtocolState state) {
        Logger.info("State transitioned: {}", state.name());
        this.state = state;
    }

}
