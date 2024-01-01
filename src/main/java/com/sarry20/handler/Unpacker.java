package com.sarry20.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.AttributeKey;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.BundlerInfo;

import java.util.List;

public class Unpacker extends MessageToMessageEncoder<Packet<?>> {
    private final AttributeKey<? extends BundlerInfo> bundlerAttributeKey;

    public Unpacker(AttributeKey<? extends BundlerInfo> p_297828_) {
        this.bundlerAttributeKey = p_297828_;
    }

    protected void encode(ChannelHandlerContext cnx, Packet<?> packet, List<Object> msg) throws Exception {
        BundlerInfo bundlerinfo$provider = cnx.channel().attr(this.bundlerAttributeKey).get();
        if (bundlerinfo$provider == null) {
            throw new EncoderException("Bundler not configured: " + packet);
        } else {
            bundlerinfo$provider.a(packet, msg::add);
        }
    }
}
