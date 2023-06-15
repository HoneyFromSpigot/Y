package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import org.bukkit.command.CommandSender;

public class SilenceCommand {
    @Command(identifier = "silence", permissions = Perms.COMMAND_SILENCE, onlyPlayers = false)
    public void onSilenceCommand(CommandSender sender) {
        YPlugin.getInstance().getConfiguration().silenceChat(true);
        Chat.messageAll(Messages.CHAT_SILENCED);
    }
}
