package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class TeleportHereCommand {
    @Command(identifier = "tphere", permissions = Perms.COMMAND_TELEPORT_HERE)
    public void onTeleportHereCommand(Player player, @Arg(name = "player") Player target) {
        Players.teleport(target, player);
        Chat.message(target, Messages.playerTeleportedToPlayer(player.getName()));
    }
}
