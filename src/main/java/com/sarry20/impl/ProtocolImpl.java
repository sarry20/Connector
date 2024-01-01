package com.sarry20.impl;

import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract;

public class ProtocolImpl extends ProtocolManagerAbstract {
    @Override
    public ProtocolVersion getPlatformVersion() {
        return ProtocolVersion.UNKNOWN;
    }
}
