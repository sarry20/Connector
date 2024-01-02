package com.sarry20;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;

public class Sender implements PacketListener {
    @Override
    public void onPacketSend(PacketSendEvent event) {
        System.out.println(event);
        if (event.getPacketType() == PacketType.Play.Client.KEEP_ALIVE){
            WrapperPlayServerKeepAlive keepAlive = new WrapperPlayServerKeepAlive(event);
            WrapperPlayClientKeepAlive alive = new WrapperPlayClientKeepAlive(keepAlive.getId());
            event.getUser().sendPacket(alive);
        }
    }
//    @Override
//    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
//        System.out.println(event);
//        System.out.println(event.getPacketType());
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
