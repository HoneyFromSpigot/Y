package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.chat.PrivateMessage;
import io.github.thewebcode.yplugin.chat.PrivateMessageManager;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.Wildcard;
import io.github.thewebcode.yplugin.permission.Perms;
import org.bukkit.entity.Player;

public class MessageCommand {
    private PrivateMessageManager pmManager;

    public MessageCommand() {
        pmManager = YPlugin.getInstance().getPrivateMessageManager();
    }

    @Command(identifier = "msg", permissions = {Perms.COMMAND_MESSAGE})
    public void onMessageCommand(Player player, @Arg(name = "receiver") Player target, @Wildcard @Arg(name = "message") String message) {
        messagePlayer(target, player, message);
    }

    private void messagePlayer(Player playerSendingTo, Player playerSendingFrom, String message) {
        Chat.sendMessage(playerSendingTo, "&f[&e" + playerSendingFrom.getDisplayName() + "&b -> &aYou&f] " + message);
        Chat.sendMessage(playerSendingFrom, "&f[&eYou &b-> &a" + playerSendingTo.getDisplayName() + "&f] " + message);
        pmManager.setRecentPrivateMessageFrom(playerSendingTo.getName(), new PrivateMessage(playerSendingFrom.getName(), playerSendingTo.getName()));
    }
}
