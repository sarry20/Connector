package com.sarry20.handler;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class Decoder extends MessageToMessageDecoder<ByteBuf> {
    public User user;

    public Decoder(User user){
        this.user = user;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.isReadable()) {
            ByteBuf outputBuffer = ctx.alloc().buffer().writeBytes(byteBuf);
            try {
                PacketSendEvent sendEvent = PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), user, null, outputBuffer, true);
                if (outputBuffer.isReadable())
                    out.add(outputBuffer.retain());

            }
            finally {
                outputBuffer.release();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
