package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import org.bukkit.entity.Player;

public class DebugActionMessage implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        Chat.actionMessage(player, "Looks like the NMS Wrapper works!");
    }

    @Override
    public String getActionName() {
        return "action_message";
    }
}
