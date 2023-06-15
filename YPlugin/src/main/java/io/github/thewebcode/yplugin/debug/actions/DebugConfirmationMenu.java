package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.entity.Entities;
import io.github.thewebcode.yplugin.menus.confirmation.ConfirmationMenu;
import io.github.thewebcode.yplugin.time.TimeType;
import org.bukkit.entity.Player;

public class DebugConfirmationMenu implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        ConfirmationMenu.of("Smite yourself?")
                .onConfirm((m, p) -> {
                    p.getWorld().strikeLightning(p.getLocation());
                    m.closeMenu(p);
                }).onDeny((m, p) -> {
            Entities.burn(p, 1, TimeType.MINUTE);
            m.closeMenu(p);
        }).exitOnClickOutside(false).denyOnClose()
                .openMenu(player);
    }

    @Override
    public String getActionName() {
        return "confirmation_menu";
    }
}
