package io.github.thewebcode.y.networking;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.thewebcode.y.YFabricMod;
import io.github.thewebcode.y.gui.LoginGuiDescription;
import io.github.thewebcode.y.gui.LoginScreen;
import io.github.thewebcode.y.networking.packet.HandshakeC2SPacket;
import io.github.thewebcode.y.networking.packet.HelloC2SPacket;
import io.github.thewebcode.y.networking.packet.SettingUpdateC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier Y_FABRIC_HELLO = new Identifier("yfabric", "hello");
    public static final Identifier UPDATE_SETTINGS = new Identifier("yfabric", "update_settings");
    public static final Identifier START_LOGIN = new Identifier("yplugin", "start_login");
    public static final Identifier HANDSHAKE_C2S = new Identifier("yfabric", "handshake_c2s");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(Y_FABRIC_HELLO, HelloC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(UPDATE_SETTINGS, SettingUpdateC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(HANDSHAKE_C2S, HandshakeC2SPacket::receive);
    }

    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(START_LOGIN, (client, handler, buf, responseSender) -> {
            MinecraftClient.getInstance().execute(() -> {
                MinecraftClient.getInstance().setScreen(new LoginScreen(new LoginGuiDescription()));
            });
        });
    }
}
