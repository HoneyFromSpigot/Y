package io.github.thewebcode.yplugin.game.gadget;

import io.github.thewebcode.yplugin.item.ItemBuilder;
import io.github.thewebcode.yplugin.inventory.menu.Menu;
import io.github.thewebcode.yplugin.inventory.menu.ItemMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MenuGadget extends ItemGadget {

    private Menu menu;

    public MenuGadget(ItemBuilder builder, Menu menu) {
        super(builder);
        this.menu = menu;
    }

    public MenuGadget(ItemStack item, Menu menu) {
        super(item);
        this.menu = menu;
    }

    @Override
    public void perform(Player holder) {
        menu.openMenu(holder);
    }
}
