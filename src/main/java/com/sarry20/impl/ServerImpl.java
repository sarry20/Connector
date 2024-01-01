package com.sarry20.impl;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.impl.netty.manager.server.ServerManagerAbstract;

public class ServerImpl extends ServerManagerAbstract {
    @Override
    public ServerVersion getVersion() {
        return ServerVersion.V_1_19_4;
    }
}
