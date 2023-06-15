package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class FeedCommand {
    @Command(identifier = "feed", permissions = Perms.COMMAND_FEED)
    public void feedCommand(Player player, @Arg(name = "player", def = "?sender") Player target) {
        Players.feed(target);
        if (target.equals(player)) {
            Chat.message(player, "&aYou've fully fed yourself! &eMmmmm!");
            return;
        }

        Chat.message(target, Messages.PLAYER_FED);
        Chat.message(player, Messages.playerFed(target.getName()));
    }
}
