package io.github.thewebcode.yplugin.utils;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.protocol.game.PacketPlayInCustomPayload;

public class PacketDataUtil {
    public static void handlePacket(String name, PacketPlayInCustomPayload packet){
        System.out.println("Packet received: " + name);
        switch (name){
            case "update_settings":
                String packetValue = readBufAsString(getByteBuf(packet));
                String[] packetValues = packetValue.split("\\|");
                String settingName = packetValues[0];
                String settingValue = packetValues[1];
                String dataKey = packetValues[2];

                //TODO: Update the Server Setting
                break;
        }
    }

    public static String readBufAsString(ByteBuf buf){
        int length = buf.readableBytes();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes);
    }

    public static ByteBuf getByteBuf(PacketPlayInCustomPayload packet){
        return packet.d;
    }
}
