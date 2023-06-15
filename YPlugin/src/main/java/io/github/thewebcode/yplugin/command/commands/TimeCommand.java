package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.world.WorldTime;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class TimeCommand {
    @Command(identifier = "time", permissions = Perms.COMMAND_TIME)
    public void onTimeCommand(CommandSender sender,@Arg(name = "time") String time, @Arg(name = "world", def = "?sender") World world) {
        time = time.toLowerCase();
        switch (time) {
            case "day":
            case "night":
            case "dawn":
                Worlds.setTime(world, WorldTime.getWorldTime(time));
                Chat.message(sender, Messages.timeUpdated(world.getName(), time));
                break;
            default:
                Chat.message(sender, Messages.invalidCommandUsage("Time [day/night/dawn]"));
                break;
        }
    }
}
