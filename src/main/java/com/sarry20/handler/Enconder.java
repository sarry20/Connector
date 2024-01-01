package com.sarry20.handler;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Enconder extends MessageToByteEncoder<ByteBuf> {
    public User user;

    public Enconder(User user){
        this.user = user;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, ByteBuf out) throws Exception {
        if (byteBuf.isReadable()) {
            out.writeBytes(byteBuf);
            PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), user, null, out, true);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
