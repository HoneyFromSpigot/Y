package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class TeleportPositionCommand {
    @Command(identifier = "tppos", permissions = Perms.COMMAND_TELEPORT_POSITION)
    public void onTeleportPositionCommand(Player player, @Arg(name = "x") double x, @Arg(name = "y") double y, @Arg(name = "z") double z) {
        Players.teleport(player, new double[]{x, y, z});
        Chat.message(player, Messages.playerTeleportedTo(String.format("%sx %sy %sz", x, y, z)));
    }
}
