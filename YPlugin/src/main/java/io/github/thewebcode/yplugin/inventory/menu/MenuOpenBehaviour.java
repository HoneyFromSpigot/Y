package io.github.thewebcode.yplugin.inventory.menu;

import io.github.thewebcode.yplugin.inventory.menu.Menu;
import org.bukkit.entity.Player;

public interface MenuOpenBehaviour extends MenuBehaviour {
    @Override
    public void doAction(Menu menu, Player player);
}
