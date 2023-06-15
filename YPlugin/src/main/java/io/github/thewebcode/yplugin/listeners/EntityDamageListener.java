package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private Configuration config;

    private Players playerHandler;

    public EntityDamageListener() {
        config = YPlugin.getInstance().getConfiguration();

        playerHandler = YPlugin.getInstance().getPlayerHandler();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageEvent(EntityDamageEvent e) {
        EntityDamageEvent.DamageCause cause = e.getCause();

        Entity damaged = e.getEntity();
        if (!(damaged instanceof Player)) {
            return;
        }

        if (!config.enableFallDamage()) {
            if (cause == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        }

        Player damagedPlayer = (Player) damaged;
        MinecraftPlayer mcPlayer = playerHandler.getData(damagedPlayer);

        if (mcPlayer.hasGodMode()) {
            e.setCancelled(true);
        }
    }
}
