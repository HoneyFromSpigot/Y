package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.debug.gadget.FlamingEnderSword;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class DebugFlamingEnderSword implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        Players.giveItem(player, FlamingEnderSword.getInstance().getItem());
        Chat.message(player,"&aID Of the Flaming Ender Sword: " + FlamingEnderSword.getInstance().id());
    }

    @Override
    public String getActionName() {
        return "ender_sword";
    }
}
