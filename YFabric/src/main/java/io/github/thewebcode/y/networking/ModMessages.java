package io.github.thewebcode.y.networking;

import io.github.thewebcode.y.networking.packet.HelloC2SPacket;
import io.github.thewebcode.y.networking.packet.SettingUpdateC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier Y_FABRIC_HELLO = new Identifier("yfabric", "hello");
    public static final Identifier UPDATE_SETTINGS = new Identifier("yfabric", "update_settings");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(Y_FABRIC_HELLO, HelloC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(UPDATE_SETTINGS, SettingUpdateC2SPacket::receive);
    }

    public static void registerS2CPackets(){

    }
}
