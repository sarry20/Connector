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
import com.sarry20.handler.*;
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
    private static User USER;

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
    private static void generatePlayer(){
        getChannel();

        WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(762,"localhost",25565, ConnectionState.LOGIN);
        WrapperLoginClientLoginStart start = new WrapperLoginClientLoginStart(ClientVersion.V_1_19_4,USER.getName(),null, USER.getUUID());

        PACKET_EVENTS_API.getProtocolManager().sendPacket(USER.getChannel(),handshake);
        PACKET_EVENTS_API.getProtocolManager().sendPacket(USER.getChannel(),start);
    }

    private static void getChannel() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY,true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    User user = new User(ch,ConnectionState.HANDSHAKING, null, new UserProfile(UUID.nameUUIDFromBytes(NAME.getBytes()),NAME));
                    USER = user;
                    System.out.println(user);
                    Decoder decoder = new Decoder(user);
                    Enconder enconder = new Enconder(user);
                    AttributeKey<?> clientKey = AttributeKey.valueOf("clientbound_protocol");
                    AttributeKey<?> serverKey = AttributeKey.valueOf("serverbound_protocol");
                    ch.pipeline()
                    .addLast("splitter", new Splitter())
                    .addLast(PacketEvents.DECODER_NAME,decoder)
                    .addLast("prepender", new Prepender())
                    .addLast(PacketEvents.ENCODER_NAME,enconder)
                    .addLast("unbundler", new Unpacker((AttributeKey<? extends BundlerInfo>) serverKey))
                    .addLast("bundler", new Packer((AttributeKey<? extends BundlerInfo>) clientKey))
                    ;
                }
            });
            b.connect("localhost",25565).sync();
        }catch (Exception e){

        }
    }
}