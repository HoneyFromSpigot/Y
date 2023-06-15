package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.debug.Debugger;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreProcessListener implements Listener {


    private static YPlugin yPlugin = YPlugin.getInstance();

    private static Configuration config = yPlugin.getConfiguration();

    public CommandPreProcessListener() {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        String command = e.getMessage();
        if (StringUtil.startsWithIgnoreCase(command, "/bukkit:") && !config.enableBukkitCommands()) {
            e.setCancelled(true);
            Chat.message(player, Messages.COMMAND_DISABLED);
            return;
        }
        switch (command) {
            case "/pl":
            case "/plugins":
            case "/plugin":
                if (!config.enablePluginsCommand() && !player.isOp()) {
                    e.setCancelled(true);
                    Chat.message(player, Messages.COMMAND_DISABLED);
                    return;
                }
                break;
            default:
                break;
        }

        MinecraftPlayer minecraftPlayer = yPlugin.getPlayerHandler().getData(player);

        if (minecraftPlayer.isInDebugMode()) {
            Debugger.debugCommandPreProcessEvent(player, e);
        }
    }
}
