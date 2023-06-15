package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {
    private Configuration config;

    private static YPlugin commons = YPlugin.getInstance();

    public PlayerQuitListener() {
        config = commons.getConfiguration();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!config.enableJoinMessages()) {
            event.setQuitMessage(null);
        }
        Players.removeData(playerId);
    }
}
