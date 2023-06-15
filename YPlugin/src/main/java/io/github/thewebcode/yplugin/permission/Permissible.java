package io.github.thewebcode.yplugin.permission;

import org.bukkit.entity.Player;


public interface Permissible {
    public String getPermission();

    public boolean hasPermission(Player p);
}
