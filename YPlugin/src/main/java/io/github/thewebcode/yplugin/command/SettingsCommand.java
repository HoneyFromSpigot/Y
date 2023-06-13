package io.github.thewebcode.yplugin.command;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.utils.LanguageService;
import io.github.thewebcode.yplugin.utils.RemoteSessionManager;
import io.github.thewebcode.yplugin.utils.ServerSettingService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(LanguageService.get(LanguageService.Language.DEFAULT, LanguageService.MessageKey.COMMAND_PLAYER_ONLY, true));
            return false;
        }

        Player player = (Player) commandSender;

        RemoteSessionManager remoteSessionManager = YPlugin.getInstance().getRemoteSessionManager();
        boolean validKey = remoteSessionManager.hasValidKey(player.getName());

        if(validKey){
            ServerSettingService.get().openSettings(player);
            return true;
        }

        player.sendMessage(LanguageService.get(LanguageService.MessageKey.ERROR_NO_VALID_RSK, true));
        return false;
    }
}
