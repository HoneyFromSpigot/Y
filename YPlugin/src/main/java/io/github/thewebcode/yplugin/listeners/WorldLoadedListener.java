package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadedListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldLoaded(WorldLoadEvent event) {
        YPlugin.getInstance().getWorldHandler().handleWeather(event.getWorld());
    }
}
