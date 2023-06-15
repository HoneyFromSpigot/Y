package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

	private Configuration config;

	public PlayerLoginListener() {
		config = YPlugin.getInstance().getConfiguration();
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (config.isMaintenanceModeEnabled()) {
			if (!Players.hasPermission(player, Perms.MAINTENANCE_WHITELIST)) {
				event.setKickMessage(config.maintenanceModeKickMessage());
				event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			}
		}
	}
}
