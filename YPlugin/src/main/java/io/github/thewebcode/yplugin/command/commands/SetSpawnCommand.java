package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.entity.Player;


public class SetSpawnCommand {
    @Command(identifier = "setspawn", permissions = {Perms.COMMAND_SETSPAWN})
    public void setSpawnCommand(Player player) {
        if (Worlds.setSpawn(player.getWorld(), player.getLocation())) {
            Chat.message(player, "&aSpawn location for the world &7" + player.getWorld().getName() + "&a has been set!");
        } else {
            Chat.message(player, "&eThere was an error changing the spawn location for world &7" + player.getWorld().getName() + "&e; please check the console.");
        }
    }
}
