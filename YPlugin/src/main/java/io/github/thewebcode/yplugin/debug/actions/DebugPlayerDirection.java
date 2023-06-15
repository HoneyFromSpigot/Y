package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import org.bukkit.entity.Player;

public class DebugPlayerDirection implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        Chat.message(player, Messages.playerFacingDirection(player));
    }

    @Override
    public String getActionName() {
        return "player_direction";
    }
}
