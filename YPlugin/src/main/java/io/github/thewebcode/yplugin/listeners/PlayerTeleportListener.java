package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.location.PreTeleportType;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    private Players players;

    public PlayerTeleportListener() {
        players = YPlugin.getInstance().getPlayerHandler();
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Location fromLocation = event.getFrom();
        Player player = event.getPlayer();
        if (fromLocation != null && players.hasData(player.getUniqueId())) {
            MinecraftPlayer mcPlayer = players.getData(player);
            PreTeleportType preTeleportType = PreTeleportType.getByCause(event.getCause());
            mcPlayer.setPreTeleportLocation(event.getFrom(), preTeleportType);
        }
    }
}
