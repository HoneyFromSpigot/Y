package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.debug.gadget.ProtoExplosionArrow;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DebugExplosionArrow implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        if (!Gadgets.hasBeenRegistered(ProtoExplosionArrow.getInstance())) {
            Gadgets.registerGadget(ProtoExplosionArrow.getInstance());
        }

        ItemStack arrow = ProtoExplosionArrow.getInstance().getItem().clone();
        arrow.setAmount(64);
        Players.giveItem(player, Items.makeItem(Material.BOW), arrow);
    }

    @Override
    public String getActionName() {
        return "explosion_arrow";
    }
}
