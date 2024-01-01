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
            ByteBuf outputBuffer = ctx.alloc().buffer().writeBytes(byteBuf);
            try {
                PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), user, null, outputBuffer, true);
                if (outputBuffer.isReadable()) {
                    out.writeBytes(outputBuffer.retain());
                }
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
