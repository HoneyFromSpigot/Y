package io.github.thewebcode.yplugin.command.commands;

import com.mysql.cj.util.StringUtils;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.chat.PrivateMessage;
import io.github.thewebcode.yplugin.chat.PrivateMessageManager;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.Wildcard;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class QuickResponseCommand {

    private static PrivateMessageManager pmManager;

    public QuickResponseCommand() {
        pmManager = YPlugin.getInstance().getPrivateMessageManager();
    }

    @Command(identifier = "r", permissions = Perms.COMMAND_MESSAGE)
    public void quickRespondMessage(Player player, @Wildcard @Arg(name = "message") String message) {
        String playerName = player.getName();

        if (StringUtils.isNullOrEmpty(message)) {
            Chat.message(player, Messages.MESSAGE_REQUIRED);
            return;
        }

        if (!pmManager.hasRecentPrivateMessageFrom(playerName)) {
            Chat.sendMessage(player, Messages.NO_RECENT_MESSAGES);
            return;
        }

        String receiver = pmManager.getMostRecentPrivateMessager(playerName);

        if (!Players.isOnline(receiver)) {
            Chat.message(player, Messages.playerOffline(receiver));
            return;
        }

        Player playerSendingTo = Players.getPlayer(receiver);


        Chat.message(playerSendingTo, "&r[&e" + player.getName() + "&b -> &aYou&r] " + message);
        Chat.message(player, "&r[&eYou&b -> &a" + playerSendingTo.getName() + "&r] " + message);
        pmManager.setRecentPrivateMessageFrom(playerSendingTo.getName(), new PrivateMessage(player.getName(), playerSendingTo.getName()));
    }
}
