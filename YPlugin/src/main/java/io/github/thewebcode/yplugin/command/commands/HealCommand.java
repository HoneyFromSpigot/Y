package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class HealCommand {
    @Command(identifier = "heal", permissions = {Perms.COMMAND_HEAL})
    public void onHealCommand(Player player, @Arg(name = "target", def = "?sender") Player target) {
        Players.heal(target);
        Chat.message(target, Messages.PLAYER_HEALED);
        if (!target.getUniqueId().equals(player.getUniqueId())) {
            if (!player.hasPermission(Perms.COMMAND_HEAL_OTHER)) {
                Chat.message(player, Messages.permissionRequired(Perms.COMMAND_HEAL_OTHER));
                return;
            }
            Chat.message(player, Messages.playerHealed(target.getName()));
        }
    }
}
