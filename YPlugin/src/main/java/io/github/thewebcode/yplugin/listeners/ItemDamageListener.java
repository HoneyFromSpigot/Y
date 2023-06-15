package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.game.gadget.Gadget;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDamageListener implements Listener {

    @EventHandler
    public void itemDamageEvent(PlayerItemDamageEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (!Gadgets.isGadget(item)) {
            return;
        }

        Gadget gadget = Gadgets.getGadget(item);
        if (!gadget.properties().isBreakable()) {
            e.setCancelled(true);
        }
    }
}
