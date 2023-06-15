package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import org.bukkit.entity.Player;

public class TeleportMenuCommand {

    @Command(identifier = "tpmenu")
    public void onTpMenuCommand(Player player, @Arg(name = "option") String option) {
        switch (option) {
            case "enable":
            case "on":
                YPlugin.TeleportMenuSettings.getInstance().enableMenu(player.getUniqueId());
                Chat.actionMessage(player, "&aYour teleport menus has been enabled!");
                break;
            case "disable":
            case "off":
                YPlugin.TeleportMenuSettings.getInstance().disableMenu(player.getUniqueId());
                Chat.actionMessage(player, "&cYour teleport menus has been disabled");
                break;
            default:
                Chat.message(player, Messages.invalidCommandUsage("(enable/on) / (disable/off)"));
                break;
        }
    }

}
