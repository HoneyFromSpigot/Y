package io.github.thewebcode.yplugin.config;

import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ColorCode {
    BLACK("yplugin.color.black", ChatColor.BLACK),
    DARK_BLUE("yplugin.color.darkblue", ChatColor.DARK_BLUE),
    BLUE("yplugin.color.blue", ChatColor.BLUE),
    GREEN("yplugin.color.green", ChatColor.GREEN),
    DARK_GREEN("yplugin.color.darkgreen", ChatColor.DARK_GREEN),
    GOLD("yplugin.color.gold", ChatColor.GOLD),
    GRAY("yplugin.color.gray", ChatColor.GRAY),
    DARK_GRAY("yplugin.color.darkgray", ChatColor.DARK_GRAY),
    RED("yplugin.color.red", ChatColor.RED),
    DARK_RED("yplugin.color.darkred", ChatColor.DARK_RED),
    LIGHT_PURPLE("yplugin.color.lightpurple", ChatColor.LIGHT_PURPLE),
    DARK_PURPLE("yplugin.color.darkpurple", ChatColor.DARK_PURPLE),
    YELLOW("yplugin.color.yellow", ChatColor.YELLOW),
    WHITE("yplugin.color.white", ChatColor.WHITE);

    private String permission;
    private ChatColor color;

    private static Map<String, ChatColor> permissionColors = new HashMap<>();
    private static Map<String, ChatColor> namedColors = new HashMap<>();

    static {
        for (ColorCode colorCode : EnumSet.allOf(ColorCode.class)) {
            permissionColors.put(colorCode.permission, colorCode.color);
            namedColors.put(colorCode.name(), colorCode.getColor());
        }
    }

    ColorCode(String permission, ChatColor color) {
        this.color = color;
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public static ChatColor getColorForPermission(String permission) {
        return permissionColors.get(permission.toLowerCase());
    }

    public static ChatColor getColorByName(String name) {
        return namedColors.get(name);
    }

    public static boolean isColor(String name) {
        return namedColors.containsKey(name);
    }

    public static Collection<String> colorNames() {
        return namedColors.keySet();
    }
}
