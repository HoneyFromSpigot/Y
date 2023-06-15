package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.game.gadget.Gadget;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDropListener implements Listener {
    private Configuration config;

    public ItemDropListener() {
        config = YPlugin.getInstance().getConfiguration();
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (!Gadgets.isGadget(item)) {
            if (!config.enableItemDrop()) {
                event.setCancelled(true);
                return;
            }
        }

        Gadget gadget = Gadgets.getGadget(item);
        if (gadget == null) {
            return;
        }

        if (!gadget.properties().isDroppable()) {
            event.setCancelled(true);
            gadget.onDrop(event.getPlayer(), null);
            return;
        }

        gadget.onDrop(event.getPlayer(), event.getItemDrop());
    }
}
