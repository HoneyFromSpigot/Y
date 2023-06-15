package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.debug.DebugAction;
import org.bukkit.entity.Player;

public class DebugGadgetMenu implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {

    }

    @Override
    public String getActionName() {
        return "gadgets_menu";
    }
}
