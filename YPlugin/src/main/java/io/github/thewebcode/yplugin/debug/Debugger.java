package io.github.thewebcode.yplugin.debug;

import com.google.common.base.Splitter;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.chat.menu.HelpScreen;
import io.github.thewebcode.yplugin.chat.menu.ItemFormat;
import io.github.thewebcode.yplugin.chat.menu.PageDisplay;
import io.github.thewebcode.yplugin.inventory.menu.Menus;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.location.Locations;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Debugger {
    private static Map<String, DebugAction> debugActions = new HashMap<>();
    private static final String BLOCK_BREAK_MESSAGE = "&9@[%sx,%sy,%sz]: &e#%s &7(%s) &r[%s] (%s/15)";
    private static final String INVENTORY_CLICK_MESSAGE = "&7[%s / %s] (%s / %s)";
    private static final String COMMAND_PREPROCESS_MESSAGE = "&7/%s &r has &l%s&r arguments %s&7";
    private static HelpScreen debuggerHelpScreen = null;

    public static void addDebugAction(DebugAction... actions) {
        for (DebugAction action : actions) {
            debugActions.put(action.getActionName().toLowerCase(), action);
        }
    }

    public static void addDebugActionsByPackage(String pkg) {
        Reflections reflect = new Reflections(pkg);
        Set<Class<? extends DebugAction>> debugActionClasses = reflect.getSubTypesOf(DebugAction.class);

        if (debugActionClasses.isEmpty()) {
            Chat.debug("Unable to find any DebugActions in the package '" + pkg + "'");
            return;
        }

        StringBuilder actionNames = new StringBuilder();
        Chat.debug("Found " + debugActionClasses.size() + " Classes that implement DebugAction");
        for (Class<? extends DebugAction> actionClass : debugActionClasses) {
            try {
                DebugAction action = Reflect.on(actionClass).create().get();
                debugActions.put(action.getActionName().toLowerCase(), action);
                actionNames.append(action.getActionName()).append(" ");
            } catch (ReflectException e) {
                e.printStackTrace();
            }
        }

        Chat.debug("Registered the following debug actions: " + actionNames.toString());
    }

    public static boolean isDebugAction(String name) {
        return debugActions.containsKey(name.toLowerCase());
    }

    public static DebugAction getDebugAction(String name) {
        return debugActions.get(name.toLowerCase());
    }

    public static HelpScreen getDebugMenu() {
        if (debuggerHelpScreen == null) {
            debuggerHelpScreen = Menus.generateHelpScreen("Debugger - Actions List", PageDisplay.DEFAULT, ItemFormat.NO_DESCRIPTION, ChatColor.GREEN, ChatColor.DARK_GREEN);
            for (String action : debugActions.keySet()) {
                debuggerHelpScreen.setEntry("/debug " + action, "");
            }
        }
        return debuggerHelpScreen;
    }

    public static void debugEvent(Player player, Event event) {
        if (event instanceof BlockBreakEvent) {
            debugBlockBreakEvent(player, (BlockBreakEvent) event);
        } else if (event instanceof InventoryClickEvent) {
            debugInventoryClickEvent(player, (InventoryClickEvent) event);
        }
    }


    public static void debugBlockBreakEvent(Player player, BlockBreakEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        String blockName = Items.getFormattedMaterialName(block.getType());
        int[] blockCords = Locations.getXYZ(block.getLocation());
        String debugMessage = String.format(BLOCK_BREAK_MESSAGE, blockCords[0], blockCords[1], blockCords[2], blockType.getId(), blockName, block.getState().getData().getData(), block.getLightLevel());
        Chat.message(player, debugMessage);
    }

    public static void debugInventoryClickEvent(Player player, InventoryClickEvent event) {
        String action = event.getAction().toString();
        String clickType = event.getClick().toString();
        int rawSlot = event.getRawSlot();
        int slot = event.getSlot();
        String inventoryMessage = String.format(INVENTORY_CLICK_MESSAGE, slot, rawSlot, clickType, action);
        Chat.message(player, inventoryMessage);
    }

    public static void debugCommandPreProcessEvent(Player player, PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        String addition = "";
        String[] args = command.split(" ");
        int argLength = args.length;
        if (argLength > 0) {
            for (int i = 0; i < argLength; i++) {
                addition += "[" + i + " -> " + args[i] + "]" + (i == (argLength - 1) ? "" : ", ");
            }
        }
        String message = String.format(COMMAND_PREPROCESS_MESSAGE, command, argLength, addition);
        Chat.message(player, message);
    }

    private static Splitter bookSplitter = Splitter.fixedLength(250).limit(50);

    public static ItemStack createExceptionBook(Throwable ex) {
        List<String> bookPages = bookSplitter.splitToList(StringUtil.getStackStr(ex));
        return Items.makeBook(ex.getClass().getCanonicalName(), "ERROR", bookPages);
    }
}
