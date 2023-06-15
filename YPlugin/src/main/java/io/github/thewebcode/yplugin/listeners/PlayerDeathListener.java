package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.location.PreTeleportType;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private static Players players;

    public PlayerDeathListener() {
        players = YPlugin.getInstance().getPlayerHandler();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        MinecraftPlayer minecrafter = players.getData(player);

        minecrafter.setPreTeleportLocation(player.getLocation(), PreTeleportType.DEATH);

        if (minecrafter.hasForceRespawn()) {
            try {
                Players.forceRespawn(player);
                Chat.message(player,"&7Forced respawn!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
