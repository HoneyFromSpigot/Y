package io.github.thewebcode.y;

import io.github.thewebcode.y.networking.ModMessages;
import io.github.thewebcode.y.networking.packet.HelloC2SPacket;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YFabricMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("yfabric");

	@Override
	public void onInitialize() {
		ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionEvents.Join() {
			@Override
			public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
				System.out.println("Sending hello packet");
				ClientPlayNetworking.send(ModMessages.Y_FABRIC_HELLO, PacketByteBufs.create());
			}
		});
		ModMessages.registerC2SPackets();
		LOGGER.info("Enabling YFabric!");
	}
}