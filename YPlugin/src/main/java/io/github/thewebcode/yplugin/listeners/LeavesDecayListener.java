package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeavesDecayListener implements Listener {

	private Configuration config = null;

	public LeavesDecayListener() {
		config = YPlugin.getInstance().getConfiguration();
	}

	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent e) {
		if (config.disableLeavesDecay()) {
			e.setCancelled(true);
		}
	}

}
