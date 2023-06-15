package io.github.thewebcode.yplugin.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface IUserManagerHandler extends Listener {
    void handleJoin(Player player);

    void handleLeave(Player player);

    @EventHandler(priority = EventPriority.HIGHEST)
    default void onPlayerJoin(PlayerJoinEvent e) {
        handleJoin(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    default void onPlayerQuit(PlayerQuitEvent e) {
        handleLeave(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    default void onPlayerKick(PlayerKickEvent e) {
        handleLeave(e.getPlayer());
    }
}
