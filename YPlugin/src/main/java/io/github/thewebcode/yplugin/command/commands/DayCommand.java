package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class DayCommand {
    @Command(identifier = "day", permissions = Perms.COMMAND_TIME)
    public void onDayCommand(Player player) {
        World world = player.getWorld();
        Worlds.setTimeDay(world);
        Chat.message(player, Messages.timeUpdated(world.getName(), "day"));
    }
}
