package io.github.thewebcode.y;

import io.github.thewebcode.y.gui.LoginGuiDescription;
import io.github.thewebcode.y.gui.LoginScreen;
import io.github.thewebcode.y.networking.ModMessages;
import io.github.thewebcode.y.networking.packet.HelloC2SPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YFabricMod implements ModInitializer {
	private static YFabricMod instance;
	public static final String MOD_ID = "yfabric";
    public static final Logger LOGGER = LoggerFactory.getLogger("yfabric");
	public static String REMOTE_SERVER_KEY = "NONE";

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