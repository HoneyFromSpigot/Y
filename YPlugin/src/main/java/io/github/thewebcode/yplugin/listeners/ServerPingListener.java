package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener implements Listener {

	private Configuration config;

	public ServerPingListener() {
		config = YPlugin.getInstance().getConfiguration();
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent event) {
		if (config.isMaintenanceModeEnabled()) {
			event.setMotd(StringUtil.formatColorCodes(config.maintenanceModeMotd()));
		}
	}
}
