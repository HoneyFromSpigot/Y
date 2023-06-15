package io.github.thewebcode.yplugin.inventory.menu;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.menu.HelpScreen;
import io.github.thewebcode.yplugin.chat.menu.ItemFormat;
import io.github.thewebcode.yplugin.chat.menu.PageDisplay;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class Menus {
    private static int[] ROW_PLACEMENTS = new int[54];

    static {
        for (int i = 0; i < ROW_PLACEMENTS.length; i++) {
            ROW_PLACEMENTS[i] = getRows(i);
        }
    }

    public static int getRows(int itemCount) {
        return ((int) Math.ceil(itemCount / 9.0D));
    }

    public static int getRowsForIndex(int index) {
        if (index < 0) {
            index = 0;
        }

        if (index >= ROW_PLACEMENTS.length) {
            index = 53;
        }

        return ROW_PLACEMENTS[index];
    }

    public static int getHighestIndex(ItemMenu menu) {
        return getHighestIndex(menu.getIndexedMenuItems());
    }

    public static int getHighestIndex(Map<Integer, MenuItem> menuItems) {
        return menuItems.keySet().stream().mapToInt(i -> i).max().getAsInt();
    }

    public static int getFirstFreeSlot(ItemMenu menu) {
        int highestIndex = getHighestIndex(menu);

        Map<Integer, MenuItem> items = menu.getIndexedMenuItems();
        return getFirstFreeSlot(items);
    }

    public static int getFirstFreeSlot(Map<Integer, MenuItem> items) {
        int highestIndex = getHighestIndex(items);

        int freeSlot = 0;
        for (int i = 0; i < highestIndex; i++) {
            if (items.get(i) == null) {
                freeSlot = i;
                break;
            }
        }

        if (highestIndex < 54) {
            if (freeSlot == highestIndex) {
                freeSlot++;
            }
        } else {
            return -1;
        }

        return freeSlot;
    }


    public static HelpScreen generateHelpScreen(String menuName, PageDisplay pageDisplay, ItemFormat itemFormat, ChatColor flipColorEven, ChatColor flipColorOdd) {
        HelpScreen helpScreen = new HelpScreen(menuName);
        helpScreen.setHeader(pageDisplay.toString());
        helpScreen.setFormat(itemFormat.toString());
        helpScreen.setFlipColor(flipColorEven, flipColorOdd);
        return helpScreen;
    }

    public static HelpScreen generateHelpScreen(String menuName, PageDisplay pageDisplay, ItemFormat itemFormat, ChatColor itemColor) {
        return generateHelpScreen(menuName, pageDisplay, itemFormat, itemColor, itemColor);
    }

    public static HelpScreen generateHelpScreen(String menuName, PageDisplay pageDisplay, ItemFormat itemFormat, ChatColor flipColorEven, ChatColor flipColorOdd, Map<String, String> helpItems) {
        HelpScreen helpScreen = generateHelpScreen(menuName, pageDisplay, itemFormat, flipColorEven, flipColorOdd);
        for (Map.Entry<String, String> menuItem : helpItems.entrySet()) {
            helpScreen.setEntry(menuItem.getKey(), menuItem.getValue());
        }
        return helpScreen;
    }

    public static ItemMenu createItemMenu(String title, int rows) {
        return new ItemMenu(title, rows);
    }

    public static ItemMenu cloneItemMenu(ItemMenu menu) {
        return menu.clone();
    }

    public static void removeMenu(Menu menu) {
        for (HumanEntity viewer : menu.getInventory().getViewers()) {
            if (viewer instanceof Player) {
                menu.closeMenu((Player) viewer);
            } else {
                viewer.closeInventory();
            }
        }
    }

    public static void switchMenu(final Player player, Menu fromMenu, Menu toMenu) {
        fromMenu.closeMenu(player);
        YPlugin.getInstance().getThreadManager().runTaskOneTickLater(() -> toMenu.openMenu(player));
    }
}
