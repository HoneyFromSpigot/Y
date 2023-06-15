package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.debug.gadget.KickStick;
import org.bukkit.entity.Player;

public class DebugKickStick implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        KickStick.getInstance().giveTo(player);
        Chat.message(player, "&aKick Stick is registered with ID: &e" + KickStick.getInstance().id());
    }

    @Override
    public String getActionName() {
        return "kick_stick";
    }
}
