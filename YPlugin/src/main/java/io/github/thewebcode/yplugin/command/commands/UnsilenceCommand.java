package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.permission.Perms;
import org.bukkit.command.CommandSender;

public class UnsilenceCommand {

    private static Configuration config = YPlugin.getInstance().getConfiguration();

    public UnsilenceCommand() {

    }

    @Command(identifier = "unsilence", permissions = Perms.COMMAND_SILENCE, onlyPlayers = false)
    public void unsilenceLobbyCommand(CommandSender sender) {
        config.silenceChat(false);
        Chat.messageAll(Messages.CHAT_UNSILENCED);
    }
}
