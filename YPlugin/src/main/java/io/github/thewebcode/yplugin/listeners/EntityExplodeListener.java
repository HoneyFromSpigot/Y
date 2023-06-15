package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.effect.Fireworks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {
	private Configuration config;

	public EntityExplodeListener() {
		config = YPlugin.getInstance().getConfiguration();
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled()) {
			return;
		}

		if (config.hasExplosionFireworks()) {
			Fireworks.playRandomFirework(event.getLocation());
		}

		if (config.enableBlockBreak()) {
			return;
		}
		event.blockList().clear();
	}
}
