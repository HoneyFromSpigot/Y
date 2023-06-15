package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.command.CommandSender;

public class MaintenanceCommand {
    private static Configuration config = YPlugin.getInstance().getConfiguration();

    public MaintenanceCommand() {

    }

    @Command(identifier = "maintenance", permissions = {Perms.MAINTENANCE_TOGGLE}, onlyPlayers = false)
    public void onMaintenanceCommand(CommandSender sender, @Arg(name = "action", def = "toggle") String mode) {
        switch (mode.toLowerCase()) {
            case "on":
                config.setMaintenanceMode(true);
                Players.kickAllWithoutPermission(Perms.MAINTENANCE_WHITELIST, config.maintenanceModeKickMessage());
                Chat.message(sender, Messages.MAINTENANCE_MODE_ENABLED);
                break;
            case "off":
                config.setMaintenanceMode(false);
                Chat.message(sender, Messages.MAINTENANCE_MODE_DISABLED);
                break;
            case "toggle":
                config.setMaintenanceMode(!config.isMaintenanceModeEnabled());
                if (config.isMaintenanceModeEnabled()) {
                    Players.kickAllWithoutPermission(Perms.MAINTENANCE_WHITELIST, config.maintenanceModeKickMessage());
                    Chat.message(sender, Messages.MAINTENANCE_MODE_ENABLED);
                } else {
                    Chat.message(sender, Messages.MAINTENANCE_MODE_DISABLED);
                }
                break;
            default:
                Chat.message(sender, Messages.invalidCommandUsage("status [on/off/toggle]"));
                break;
        }
    }
}
