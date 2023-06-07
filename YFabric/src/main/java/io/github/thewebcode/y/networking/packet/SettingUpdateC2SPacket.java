package io.github.thewebcode.y.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SettingUpdateC2SPacket implements DefaultPacket{
    private PacketByteBuf buf;

    public SettingUpdateC2SPacket(String setting, String value){
        this.buf = PacketByteBufs.create();
        buf.writeString(setting);
        buf.writeString("|");
        buf.writeString(value);
    }


    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

    }

    @Override
    public PacketByteBuf value() {
        return buf;
    }
}
