package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class TeleportCommand {
    @Command(identifier = "tp", permissions = Perms.COMMAND_TELEPORT)
    public void onTeleportCommand(Player sender, @Arg(name = "target") Player target) {
        Players.teleport(sender, target);
        Chat.message(sender, Messages.playerTeleportedToPlayer(target.getName()));
    }
}
