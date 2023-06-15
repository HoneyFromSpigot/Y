package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class DebugDropInventory implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        YPlugin.getInstance().debug("Dropping inventory of " + player.getName());
        Players.dropInventory(player);
    }

    @Override
    public String getActionName() {
        return "drop_inventory";
    }
}
