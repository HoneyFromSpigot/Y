package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.Debugger;
import io.github.thewebcode.yplugin.game.gadget.Gadget;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.inventory.HandSlot;
import io.github.thewebcode.yplugin.inventory.Inventories;
import io.github.thewebcode.yplugin.inventory.menu.ItemMenu;
import io.github.thewebcode.yplugin.inventory.menu.MenuAction;
import io.github.thewebcode.yplugin.inventory.menu.MenuBehaviour;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class InventoryListener implements Listener {

    private static Players playerManager = null;

    public InventoryListener() {
        playerManager = YPlugin.getInstance().getPlayerHandler();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMenuClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof ItemMenu)) {
            return;
        }
        event.setCancelled(true);
        ItemMenu menu = (ItemMenu) holder;

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            if (menu.exitOnClickOutside()) {
                menu.closeMenu(player);
            }
        }
        int index = event.getRawSlot();
        if (index < inventory.getSize()) {
            menu.selectMenuItem(player, index, event.getClick());
        } else {
            if (menu.exitOnClickOutside()) {
                menu.closeMenu(player);
            }
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryType inventoryType = inventory.getType();
        Player player = (Player) event.getWhoClicked();
        if (event.isCancelled()) {
            return;
        }

        MinecraftPlayer minecraftPlayer = playerManager.getData(player);
        switch (inventoryType) {
            case WORKBENCH:
                break;
            case PLAYER:
                PlayerInventory pInv = (PlayerInventory) inventory;
                InventoryAction action = event.getAction();
                int slot = event.getSlot();
                if (slot == Inventories.PLAYER_OFF_HAND_ITEM_SLOT) {
                    ItemStack cursorItem = event.getCursor();

                    if (cursorItem != null && cursorItem.getType() != Material.AIR) {
                        break;
                    }

                    if (Gadgets.isGadget(cursorItem)) {
                        Chat.formatDebug("Cursor item %s is a gadget", Items.getName(cursorItem));

                        Gadget movingGadget = Gadgets.getGadget(cursorItem);
                        if (!movingGadget.properties().isOffhandEquippable()) {
                            Chat.message(player, Messages.gadgetEquipError(movingGadget, HandSlot.OFF_HAND));
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
                break;
            case CHEST:
                break;
            default:
                break;
        }

        if (minecraftPlayer.isInDebugMode()) {
            Debugger.debugInventoryClickEvent(player, event);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        Player player = (Player) e.getPlayer();
        if (holder instanceof ItemMenu) {
            ItemMenu menu = (ItemMenu) holder;
            List<MenuBehaviour> openBehaviours = menu.getBehaviours(MenuAction.OPEN);
            if (openBehaviours != null) {
                openBehaviours.stream().filter(behaviour -> behaviour != null).forEach(behaviour -> behaviour.doAction(menu, player));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryType inventoryType = inventory.getType();
        Player player = (Player) event.getPlayer();
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof ItemMenu) {
            ItemMenu menu = (ItemMenu) holder;
            List<MenuBehaviour> closeBehaviours = menu.getBehaviours(MenuAction.CLOSE);
            if (closeBehaviours != null) {
                closeBehaviours.stream().filter(behaviour -> behaviour != null).forEach(behaviour -> behaviour.doAction(menu, player));
            }
        }
        MinecraftPlayer minecraftPlayer = playerManager.getData(player);
        switch (inventoryType) {
            case WORKBENCH:
                break;
            case PLAYER:
                break;
            case CHEST:
                break;
            default:
                break;
        }
    }
}
