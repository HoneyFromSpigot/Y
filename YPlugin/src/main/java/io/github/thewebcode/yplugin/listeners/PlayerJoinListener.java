package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.networking.MessageOutboundHandler;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerJoinListener implements Listener {
	private Configuration config;
	boolean trackOnline = false;

	private static YPlugin commons = YPlugin.getInstance();

	public PlayerJoinListener() {
		config = YPlugin.getInstance().getConfiguration();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setFlySpeed((float) MinecraftPlayer.DEFAULT_FLY_SPEED);
		player.setWalkSpeed((float) MinecraftPlayer.DEFAULT_WALK_SPEED);

		new MessageOutboundHandler.Builder(player).build();
		if (!config.enableJoinMessages()) {
			event.setJoinMessage(null);
		}
		commons.getPlayerHandler().addData(player);
		if (config.teleportToSpawnOnJoin()) {
			player.teleport(Worlds.getSpawn(player), PlayerTeleportEvent.TeleportCause.PLUGIN);
		}
	}
}
