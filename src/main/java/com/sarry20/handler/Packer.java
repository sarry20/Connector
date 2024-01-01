package com.sarry20.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.AttributeKey;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.Packet;

import java.util.List;

public class Packer extends MessageToMessageDecoder<Packet<?>> {
    private BundlerInfo currentBundler;
    private BundlerInfo infoForCurrentBundler;
    private final AttributeKey<? extends BundlerInfo> bundlerAttributeKey;

    public Packer(AttributeKey<? extends BundlerInfo> p_300638_) {
        this.bundlerAttributeKey = p_300638_;
    }
    protected void decode(ChannelHandlerContext p_265208_, Packet<?> p_265182_, List<Object> p_265368_) throws Exception {
        BundlerInfo bundlerinfo$provider = p_265208_.channel().attr(this.bundlerAttributeKey).get();
        if (bundlerinfo$provider == null) {
            throw new DecoderException("Bundler not configured: " + p_265182_);
        } else {
            BundlerInfo bundlerinfo = bundlerinfo$provider;
            if (this.currentBundler != null) {
                if (this.infoForCurrentBundler != bundlerinfo) {
                    throw new DecoderException("Bundler handler changed during bundling");
                }

                Packet<?> packet = this.currentBundler.a(p_265182_).a(p_265182_);
                if (packet != null) {
                    this.infoForCurrentBundler = null;
                    this.currentBundler = null;
                    p_265368_.add(packet);
                }
            } else {
                BundlerInfo bundlerinfo$bundler = (BundlerInfo) bundlerinfo.a(p_265182_);
                if (bundlerinfo$bundler != null) {
                    this.currentBundler = bundlerinfo$bundler;
                    this.infoForCurrentBundler = bundlerinfo;
                } else {
                    p_265368_.add(p_265182_);
                }
            }
        }
    }
}
