package com.sarry20.handler;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class Splitter extends ByteToMessageDecoder {
    private static final int MAX_VARINT21_BYTES = 3;
    private final ByteBuf helperBuf = Unpooled.directBuffer(3);
    protected void handlerRemoved0(ChannelHandlerContext p_299287_) {
        this.helperBuf.release();
    }

    private static boolean copyVarint(ByteBuf p_299967_, ByteBuf p_298224_) {
        for(int i = 0; i < 3; ++i) {
            if (!p_299967_.isReadable()) {
                return false;
            }

            byte b0 = p_299967_.readByte();
            p_298224_.writeByte(b0);
            if (!hasContinuationBit(b0)) {
                return true;
            }
        }
        throw new CorruptedFrameException("length wider than 21-bit");
    }
    protected void decode(ChannelHandlerContext channel, ByteBuf msg, List<Object> p_130568_) {
        msg.markReaderIndex();
        this.helperBuf.clear();
        if (!copyVarint(msg, this.helperBuf)) {
            msg.resetReaderIndex();
        } else {
            int i = ByteBufHelper.readVarInt(this.helperBuf);
            if (msg.readableBytes() < i) {
                msg.resetReaderIndex();
            } else {

                p_130568_.add(msg.readBytes(i));
            }
        }
    }
    public static boolean hasContinuationBit(byte p_299197_) {
        return (p_299197_ & 128) == 128;
    }
}
