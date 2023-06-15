package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.debug.Debugger;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreakPlaceListener implements Listener {

	private static YPlugin yPlugin = YPlugin.getInstance();
	private static Configuration config;

	public BlockBreakPlaceListener() {
		config = yPlugin.getConfiguration();
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (config.enableBlockBreak()) {
			return;
		}

		Player player = event.getPlayer();
		MinecraftPlayer minecraftPlayer = yPlugin.getPlayerHandler().getData(player);
		if (!Players.hasPermission(player, Perms.BLOCK_BREAK)) {
			event.setCancelled(true);
		}
		if (minecraftPlayer.isInDebugMode()) {
			Debugger.debugBlockBreakEvent(player, event);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();
		if (config.enableBlockBreak()) {
			return;
		}

		if (!Players.hasPermission(player, Perms.BLOCK_PLACE)) {
			if (player.getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
			}
		}

	}


}
