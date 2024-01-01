package com.sarry20.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.player.User;
import io.netty.channel.Channel;

public class Injector implements ChannelInjector {
    @Override
    public void inject() {

    }

    @Override
    public void uninject() {

    }

    @Override
    public void updateUser(Object channel, User user) {

    }

    @Override
    public void setPlayer(Object o, Object o1) {

    }

    @Override
    public boolean isProxy() {
        return false;
    }
}
