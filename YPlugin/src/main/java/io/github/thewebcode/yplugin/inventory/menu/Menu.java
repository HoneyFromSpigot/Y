package io.github.thewebcode.yplugin.inventory.menu;

import io.github.thewebcode.yplugin.inventory.menu.MenuAction;
import io.github.thewebcode.yplugin.inventory.menu.MenuBehaviour;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public interface Menu extends InventoryHolder, Cloneable {

    void addBehaviour(MenuAction action, MenuBehaviour behaviour);

    List<MenuBehaviour> getBehaviours(MenuAction action);

    boolean exitOnClickOutside();

    default void openMenu(Player player) {
        Inventory inventory = getInventory();
        if (inventory.getViewers().contains(player)) {
            return;
        }
        player.openInventory(inventory);

        List<MenuBehaviour> behaviours = getBehaviours(MenuAction.OPEN);
        if (behaviours.size() > 0) {
            for (MenuBehaviour behaviour : behaviours) {
                behaviour.doAction(this, player);
            }
        }
    }

    default void closeMenu(Player player) {
        Inventory inventory = getInventory();
        if (!inventory.getViewers().contains(player)) {
            return;
        }

        inventory.getViewers().remove(player);
        player.closeInventory();
        List<MenuBehaviour> behaviours = getBehaviours(MenuAction.CLOSE);
        if (behaviours.size() > 0) {
            for (MenuBehaviour behaviour : behaviours) {
                behaviour.doAction(this, player);
            }
        }
    }

    default void updateMenu() {
        getInventory().getViewers().stream().filter(entity -> entity instanceof Player).forEach(entity -> ((Player) entity).updateInventory());
    }

    public Menu clone();


    default List<HumanEntity> getViewers() {
        return getInventory().getViewers();
    }
}
