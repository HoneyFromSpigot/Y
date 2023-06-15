package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.FlagArg;
import io.github.thewebcode.yplugin.command.Flags;
import io.github.thewebcode.yplugin.effect.Fireworks;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.threading.RunnableManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FireworksCommand {

    @Command(identifier = "fw", permissions = {Perms.COMMAND_FIREWORKS})
    @Flags(identifier = {"a", "d"}, description = {"Amount of fireworks to launch", "Delay between each fireworks explosion"})
    public void fireworksCommand(Player player, @FlagArg("a") @Arg(name = "amount", def = "1") int amt, @FlagArg("d") @Arg(name = "delay", def = "10") int delay) {
        Location loc = player.getLocation();
        RunnableManager manager = YPlugin.getInstance().getThreadManager();
        for (int i = 0; i < amt; i++) {
            manager.runTaskLater(() -> Fireworks.playRandomFirework(loc), i * delay);
        }
    }
}
