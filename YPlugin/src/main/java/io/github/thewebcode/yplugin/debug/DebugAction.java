package io.github.thewebcode.yplugin.debug;

import io.github.thewebcode.yplugin.chat.Chat;
import org.bukkit.entity.Player;

public interface DebugAction {
    void doAction(Player player, String... args);

    String getActionName();

    default void debug(String... text) {
        Chat.debug(text);
    }
}
