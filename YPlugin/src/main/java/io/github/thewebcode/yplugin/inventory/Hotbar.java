package io.github.thewebcode.yplugin.inventory;

import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.yml.Path;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Hotbar {
    @Path("items")
    private Map<Integer, ItemStack> items = new HashMap<>();

    public Hotbar(Map<Integer, ItemStack> items) {
        this.items = items;
    }

    public Hotbar(ItemStack... items) {
        for (int i = 0; i < items.length; i++) {
            if (i >= 9) {
                break;
            }

            this.items.put(i, items[i].clone());
        }
    }

    public Hotbar set(int slot, ItemStack item) {
        if (slot >= 9) {
            slot = 8;
        }

        items.put(slot, item.clone());
        return this;
    }

    public void assign(Player player) {
        for (Map.Entry<Integer, ItemStack> hotbarEntry : items.entrySet()) {
            Players.setItem(player, hotbarEntry.getKey(), hotbarEntry.getValue());
        }
    }

    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[8];

        for (Map.Entry<Integer, ItemStack> hotbarEntry : this.items.entrySet()) {
            int index = hotbarEntry.getKey();
            if (index > items.length) {
                continue;
            }

            items[index] = hotbarEntry.getValue();
        }
        return items;
    }
}
