package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class TeleportAllCommand {
    @Command(identifier = "tpall", permissions = Perms.COMMAND_TELEPORT_ALL)
    public void onTpallCommand(Player player) {
        String name = player.getName();

        for (Player onlinePlayer : Players.allPlayersExcept(player.getUniqueId())) {
            Players.teleport(onlinePlayer, player);
            Chat.message(onlinePlayer, Messages.playerTeleportedToPlayer(name));
        }
    }
}
