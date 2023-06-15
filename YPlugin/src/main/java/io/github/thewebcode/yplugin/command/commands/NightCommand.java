package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class NightCommand {
    @Command(identifier = "night", permissions = Perms.COMMAND_TIME)
    public void onNightCommand(Player player, @Arg(name = "world", def = "?sender") World world) {
        Worlds.setTimeNight(world);
        Chat.message(player, Messages.timeUpdated(world.getName(), "night"));
    }
}
