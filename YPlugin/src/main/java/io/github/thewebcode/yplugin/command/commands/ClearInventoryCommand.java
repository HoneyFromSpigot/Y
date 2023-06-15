package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventoryCommand {
    @Command(identifier = "ci", permissions = {Perms.CLEAR_INVENTORY})
    public void onClearInventoryCommand(CommandSender commandSender, @Arg(name = "target", def = "?sender") String playerName) {
        Player player = null;

        if (playerName != null) {
            if (!commandSender.hasPermission(Perms.CLEAR_INVENTORY_OTHER)) {
                Chat.message(commandSender, Messages.permissionRequired(Perms.CLEAR_INVENTORY_OTHER));
                return;
            }
            if (Players.isOnline(playerName)) {
                player = Players.getPlayer(playerName);
            } else {
                Chat.message(commandSender, Messages.playerOffline(playerName));
                return;
            }
        }
        if (player == null && !(commandSender instanceof Player)) {
            Chat.message(commandSender, Messages.invalidCommandUsage("name"));
            return;
        }

        player = (Player) commandSender;
        Players.clearInventory(player, true);
        Chat.message(player, Messages.INVENTORY_CLEARED);
    }
}
