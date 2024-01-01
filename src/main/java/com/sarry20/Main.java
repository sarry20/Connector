package com.sarry20;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginSuccessAck;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import com.sarry20.handler.Packer;
import com.sarry20.handler.Prepender;
import com.sarry20.handler.Splitter;
import com.sarry20.handler.Unpacker;
import com.sarry20.impl.Injector;
import com.sarry20.impl.ProtocolImpl;
import com.sarry20.impl.ServerImpl;
import io.github.retrooper.packetevents.impl.netty.BuildData;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.impl.netty.channel.ChannelOperatorImpl;
import io.github.retrooper.packetevents.impl.netty.factory.NettyPacketEventsBuilder;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import net.minecraft.network.protocol.BundlerInfo;

import java.util.*;

public class Main {
    private static PacketEventsAPI<?> PACKET_EVENTS_API;
    private static final String NAME = "sarry20";

    public static void main(String[] args) {
        PacketEvents.setAPI(NettyPacketEventsBuilder.build(new BuildData("Connector"),
                new Injector(), new ProtocolImpl(), new ServerImpl(), new PlayerManagerAbstract() {
            @Override
            public int getPing(Object player) {
                return 0;
            }

            @Override
            public Object getChannel(Object player) {
                return ((User) player).getChannel();
            }
        }));
        PACKET_EVENTS_API = PacketEvents
                .getAPI();
        PACKET_EVENTS_API.getEventManager().registerListener(new Sender(), PacketListenerPriority.HIGH);
        PACKET_EVENTS_API.getSettings().bStats(false).checkForUpdates(false);
        PACKET_EVENTS_API.init();
        generatePlayer();

    }
    private static int getNumber(int length){
        Random r = new Random();
        return r.nextInt(length);
    }

    private static void generatePlayer(){
        //String name = NAMES.get(getNumber(NAMES.size()))
        User user = new User(getChannel(),ConnectionState.LOGIN,null, new UserProfile(UUID.nameUUIDFromBytes(NAME.getBytes()),NAME));
        WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(762,"localhost",25565, ConnectionState.LOGIN);
        WrapperLoginClientLoginStart start = new WrapperLoginClientLoginStart(ClientVersion.V_1_19_4,user.getName(),null, user.getUUID());

        PACKET_EVENTS_API.getProtocolManager().sendPacket(user.getChannel(),handshake);
        PACKET_EVENTS_API.getProtocolManager().sendPacket(user.getChannel(),start);
    }

    private static Channel getChannel() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            final Channel[] c = new Channel[1];
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    AttributeKey<?> clientKey = AttributeKey.valueOf("clientbound_protocol");
                    AttributeKey<?> serverKey = AttributeKey.valueOf("serverbound_protocol");
                    ch.pipeline().addLast("splitter", new Splitter())
                    .addLast("prepender", new Prepender())
                    .addLast("bundler", new Packer((AttributeKey<? extends BundlerInfo>) clientKey))
                    .addLast("unbundler", new Unpacker((AttributeKey<? extends BundlerInfo>) serverKey))
                    ;
                    c[0] = ch;
                }
            });
            b.connect("localhost",25565).sync();
            return c[0];
        }catch (Exception e){

        }
    return null;
    }
}