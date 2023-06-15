package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.debug.gadget.FishCannon;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class DebugFishCannon implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        Players.giveItem(player, FishCannon.getInstance().getItem());
        Chat.message(player,"&aFish Cannon has been registered under &e" + FishCannon.getInstance().id());
    }

    @Override
    public String getActionName() {
        return "fish_cannon";
    }
}
