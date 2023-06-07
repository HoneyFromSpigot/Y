package io.github.thewebcode.y;

import com.mojang.brigadier.CommandDispatcher;
import io.github.thewebcode.y.gui.LoginGuiDescription;
import io.github.thewebcode.y.gui.LoginScreen;
import io.github.thewebcode.y.networking.ModMessages;
import io.github.thewebcode.y.networking.packet.HelloC2SPacket;
import io.github.thewebcode.y.networking.packet.SettingUpdateC2SPacket;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YFabricMod implements ModInitializer {
	private static YFabricMod instance;
    public static final Logger LOGGER = LoggerFactory.getLogger("yfabric");

	@Override
	public void onInitialize() {
		instance = this;

		ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionEvents.Join() {
			@Override
			public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
				ClientPlayNetworking.send(ModMessages.Y_FABRIC_HELLO, new HelloC2SPacket(MinecraftClient.getInstance().player.getName().getString()).value());
			}
		});

		ModMessages.registerC2SPackets();
		LOGGER.info("Enabling YFabric!");
	}

	public void openLoginScreen(){
		MinecraftClient.getInstance().setScreen(new LoginScreen(new LoginGuiDescription()));
	}

	public static YFabricMod getInstance() {
		return instance;
	}
}