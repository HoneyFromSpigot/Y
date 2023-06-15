package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodChangeListener implements Listener {

	public FoodChangeListener() {
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		if (event.isCancelled()) {
			return;
		}

		if (YPlugin.getInstance().getConfiguration().enableFoodChange()) {
			return;
		}

		event.setCancelled(true);
	}
}
