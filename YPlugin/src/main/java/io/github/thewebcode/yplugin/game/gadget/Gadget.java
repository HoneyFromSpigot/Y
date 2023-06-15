package io.github.thewebcode.yplugin.game.gadget;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public interface Gadget extends Listener {

    ItemStack getItem();

    void perform(Player holder);

    default void onBreak(Player p) {

    }

    default void onDrop(Player player, Item item) {

    }

    default void onRightClockBlock(Player player, Block block) {
        perform(player);
    }

    <T extends GadgetProperties> T properties();

    int id();
}
