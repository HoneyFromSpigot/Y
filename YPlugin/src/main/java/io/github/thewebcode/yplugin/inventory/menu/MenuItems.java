package io.github.thewebcode.yplugin.inventory.menu;

import io.github.thewebcode.yplugin.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class MenuItems {

    public static final MenuItem DEFAULT_GRAY_WINDOW_PAIN = new MenuItem(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).item()) {
        @Override
        public void onClick(Player player, ClickType type) {
        }
    };
}
