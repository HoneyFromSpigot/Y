package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class TeleportOtherCommand {
    @Command(identifier = "tpo", permissions = Perms.COMMAND_TELEPORT_OTHER, onlyPlayers = true)
    public void onTeleportOtherCommand(Player sender, @Arg(name = "player") Player p, @Arg(name = "target") Player target) {
        Players.teleport(p, target);
        Chat.message(p, Messages.playerTeleportedToPlayer(target.getName()));
    }
}
