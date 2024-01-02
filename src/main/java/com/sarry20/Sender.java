package com.sarry20;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCompression;

public class Sender implements PacketListener {
    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.KEEP_ALIVE){
            WrapperPlayServerKeepAlive keepAlive = new WrapperPlayServerKeepAlive(event);
            WrapperPlayClientKeepAlive alive = new WrapperPlayClientKeepAlive(keepAlive.getId());
            event.getUser().sendPacket(alive);
        } else if(event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS){
            WrapperPlayClientSettings clientSettings = new WrapperPlayClientSettings("en_GB", 8, WrapperPlayClientSettings.ChatVisibility.FULL,true, (byte) 0x85, HumanoidArm.LEFT, false,true);
            event.getUser().setConnectionState(ConnectionState.PLAY);
            event.getUser().sendPacket(clientSettings);
        }else if (event.getPacketType() == PacketType.Login.Server.SET_COMPRESSION){
            WrapperPlayServerSetCompression compression = new WrapperPlayServerSetCompression(event);
            Main.setupCompression(compression.getThreshold());
        }
//        else if(event.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK){
//            WrapperPlayServerPlayerPositionAndLook p = new WrapperPlayServerPlayerPositionAndLook(event);
//            WrapperPlayClientTeleportConfirm tp = new WrapperPlayClientTeleportConfirm(p.getTeleportId());
//            event.getUser().sendPacket(tp);
//            WrapperPlayClientPlayerPositionAndRotation pos = new WrapperPlayClientPlayerPositionAndRotation(new Location(p.getX(),p.getY(), p.getZ(),p.getYaw(),p.getPitch()),true);
//            event.getUser().sendPacket(pos);
//        }
    }
//    @Override
//    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
//        if (event.getPacketType() == PacketType.Login.Server.ENCRYPTION_REQUEST){
//            try {
//                WrapperLoginServerEncryptionRequest request = new WrapperLoginServerEncryptionRequest(event);
//
//                final Cipher rsa = Cipher.getInstance("RSA");
//                rsa.init(Cipher.ENCRYPT_MODE, request.getPublicKey());
//
//                final byte[] token = new byte[16];
//                new SecureRandom().nextBytes(token);
//
//                final byte[] encryptedSecret = rsa.doFinal(token);
//                final byte[] encryptedToken = rsa.doFinal(request.getVerifyToken());
//
//                WrapperLoginClientEncryptionResponse response = new WrapperLoginClientEncryptionResponse(ClientVersion.V_1_19_4, encryptedSecret,encryptedToken);
//
//                Main.getPacketEventsApi().getProtocolManager().sendPacket(Main.channel,response);
//            }catch (Exception ex){
//                System.out.println("an error occured");
//                ex.printStackTrace();
//            }
//        }else if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS){
//            WrapperLoginClientLoginSuccessAck ack = new WrapperLoginClientLoginSuccessAck();
//            Main.getPacketEventsApi().getProtocolManager().sendPacket(Main.channel,ack);
//
//        }
//    }
}
