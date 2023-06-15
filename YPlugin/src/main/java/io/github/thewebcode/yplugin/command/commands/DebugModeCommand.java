package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.Wildcard;
import io.github.thewebcode.yplugin.debug.Debugger;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class DebugModeCommand {

    private Players players;

    public DebugModeCommand() {
        players = YPlugin.getInstance().getPlayerHandler();
    }

    @Command(identifier = "debug on")
    public void debugOnCommand(Player player, @Arg(name = "player", def = "?sender") MinecraftPlayer mcPlayer) {
        mcPlayer.setInDebugMode(true);
        Chat.message(player, Messages.playerDebugModeChange(mcPlayer));
    }

    @Command(identifier = "debug off")
    public void debugOffCommand(Player player, @Arg(name = "player", def = "?sender") MinecraftPlayer mcPlayer) {
        mcPlayer.setInDebugMode(false);
        Chat.message(player, Messages.playerDebugModeChange(mcPlayer));
    }

    @Command(identifier = "debug ?")
    public void debugListCommand(Player player, @Arg(name = "page", def = "1") int page) {
        Debugger.getDebugMenu().sendTo(player, page, 6);
    }

    @Command(identifier = "debug", permissions = Perms.DEBUG_MODE)
    public void onDebugModeCommand(Player player, @Arg(name = "action", def = "") String action, @Wildcard @Arg(name = "arguments") String args) {
        MinecraftPlayer minecraftPlayer = players.getData(player);
        if (action == null || action.isEmpty()) {
            minecraftPlayer.setInDebugMode(!minecraftPlayer.isInDebugMode());
            Chat.message(player, Messages.playerDebugModeChange(minecraftPlayer));
            return;
        }

        if (Debugger.isDebugAction(action)) {
            String[] debugArgs = args.split(" ");
            Chat.debug("Arguments for Debug Action " + action + " are: " + args);
            Debugger.getDebugAction(action).doAction(player, debugArgs);
            return;
        }
    }
}
