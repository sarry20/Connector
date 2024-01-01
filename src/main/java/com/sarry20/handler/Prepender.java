package com.sarry20.handler;

import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;

public class Prepender extends MessageToByteEncoder<ByteBuf> {
public static final int MAX_VARINT21_BYTES = 3;
    public int getVarIntSize(int len) {
        for (int i = 1; i < 5; i++) {
            if ((len & -1 << i * 7) == 0)
                return i;
        }
        return 5;
    }
protected void encode(ChannelHandlerContext p_130571_, ByteBuf msg, ByteBuf out) {
        int i = msg.readableBytes();
        int j = getVarIntSize(i);
        if (j > 3) {
        throw new EncoderException("unable to fit " + i + " into 3");
        } else {
        out.ensureWritable(j + i);
        ByteBufHelper.writeVarInt(out, i);
        out.writeBytes(msg, msg.readerIndex(), i);
        }
        }
}
