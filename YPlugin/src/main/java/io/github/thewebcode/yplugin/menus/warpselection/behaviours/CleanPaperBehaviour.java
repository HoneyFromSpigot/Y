package io.github.thewebcode.yplugin.menus.warpselection.behaviours;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.inventory.Inventories;
import io.github.thewebcode.yplugin.inventory.menu.Menu;
import io.github.thewebcode.yplugin.inventory.menu.MenuCloseBehaviour;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class CleanPaperBehaviour implements MenuCloseBehaviour {

    private static CleanPaperBehaviour instance;

    private static final String[] itemNames = new String[]{"Next Page", "Previous Page"};
    private static final Material PAPER = Material.PAPER;

    protected CleanPaperBehaviour() {

    }

    public static CleanPaperBehaviour getInstance() {
        if (instance == null) {
            instance = new CleanPaperBehaviour();
        }
        return instance;
    }

    @Override
    public void doAction(Menu menu, final Player player) {
        YPlugin.getInstance().getThreadManager().runTaskOneTickLater(() -> {
            PlayerInventory inventory = player.getInventory();
            for (String name : itemNames) {
                int slot = Inventories.getSlotOf(inventory, PAPER, name);
                if (slot == -1) {
                    continue;
                }
                Inventories.clearSlot(inventory, slot);
            }
        });
    }
}
