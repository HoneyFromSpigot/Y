package io.github.thewebcode.yplugin.game.listener;

import io.github.thewebcode.yplugin.game.players.UserManager;
import io.github.thewebcode.yplugin.plugin.IYBukkitPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class AbstractUserManagerListener implements IUserManagerHandler {
    private UserManager userManager = null;
    private IYBukkitPlugin plugin = null;

    public AbstractUserManagerListener(IYBukkitPlugin plugin, UserManager userManager) {
        this.plugin = plugin;
        this.userManager = userManager;
    }

    public abstract void handleJoin(Player player);
    public abstract void handleLeave(Player player);

    protected IYBukkitPlugin getPlugin() {
        return plugin;
    }

    protected UserManager getUserManager() {
        return userManager;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent e) {
        handleJoin(e.getPlayer());
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent e) {
        handleLeave(e.getPlayer());
    }

    @Override
    public void onPlayerKick(PlayerKickEvent e) {
        handleLeave(e.getPlayer());
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent e) {
        try {
            userManager.getUser(e.getPlayer()).updateWorld();
            plugin.getPluginLogger().info("Updated world for " + e.getPlayer().getName());
        } catch (NullPointerException ex) {
            plugin.getThreadManager().runTaskLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        userManager.getUser(e.getPlayer()).updateWorld();
                    } catch (Exception ex1) {
                    }
                }
            }, 40);
        }
    }
}
