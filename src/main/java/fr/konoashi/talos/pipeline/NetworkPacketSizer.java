package fr.konoashi.talos.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import fr.konoashi.talos.network.PacketBuffer;
import fr.konoashi.talos.util.NetworkUtils;

import java.util.List;

public class NetworkPacketSizer extends ByteToMessageCodec<ByteBuf> {

    private static final int LENGTH_SIZE = 5;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        int i = in.readableBytes();
        int j = PacketBuffer.getVarIntSize(i);

        if (j > 3)
        {
            throw new IllegalArgumentException("unable to fit " + i + " into " + 3);
        }
        else
        {
            PacketBuffer packetbuffer = new PacketBuffer(out);
            packetbuffer.ensureWritable(j + i);
            packetbuffer.writeVarIntToBuffer(i);
            packetbuffer.writeBytes(in, in.readerIndex(), i);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        buf.markReaderIndex();
        int length = NetworkUtils.readVarInt(buf);
        if(buf.readableBytes() < length) {
            buf.resetReaderIndex();
            return;
        }
        out.add(buf.readBytes(length));
    }

}
