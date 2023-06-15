package io.github.thewebcode.yplugin.inventory.menu;

import io.github.thewebcode.yplugin.inventory.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface MenuItemClickHandler {
    void onClick(MenuItem item, Player player, ClickType type);
}
