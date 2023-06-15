package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.UUID;

public class PlayerKickListener implements Listener {

    private Configuration config;

    public PlayerKickListener() {
        config = YPlugin.getInstance().getConfiguration();
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event) {
        if (!config.enableKickMessages()) {
            event.setLeaveMessage(null);
        }

        Player player = event.getPlayer();

        if (Players.hasPermission(player, Perms.KICK_DENY)) {
            event.setCancelled(true);
            return;
        }

        UUID playerId = player.getUniqueId();
        Players.removeData(playerId);
    }
}
