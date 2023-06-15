package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private static YPlugin yPlugin = YPlugin.getInstance();

    private Configuration config;

    public ChatListener() {
        config = yPlugin.getConfiguration();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (config.isChatSilenced()) {
            if (!Players.canChatWhileSilenced(player)) {
                Chat.message(player, Messages.CHAT_SILENCED);
                event.setCancelled(true);
                return;
            }
        }
        MinecraftPlayer minecraftPlayer = yPlugin.getPlayerHandler().getData(player);

        if (config.hasExternalChatPlugin()) {
            return;
        }

        event.setFormat(StringUtil.formatColorCodes(String.format("&r%s - %s", player.getDisplayName(), event.getMessage())));
    }
}
