package io.github.thewebcode.y.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class HandshakeC2SPacket implements DefaultPacket{

    private PacketByteBuf buf;

    public HandshakeC2SPacket(String player, String pass){
        buf = PacketByteBufs.create();
        buf.writeString(player);
        buf.writeString("|");
        buf.writeString(pass);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
    }

    @Override
    public PacketByteBuf value() {
        return buf;
    }
}
