package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.debug.gadget.ThrowableBrick;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class DebugThrowableBrick implements DebugAction {

    @Override
    public void doAction(Player player, String... args) {
        Players.giveItem(player, ThrowableBrick.getInstance().getItem());
        Chat.message(player, "&aThrowable Brick is registered under id: &e" + ThrowableBrick.getInstance().id());
    }

    @Override
    public String getActionName() {
        return "brick_throw";
    }
}
