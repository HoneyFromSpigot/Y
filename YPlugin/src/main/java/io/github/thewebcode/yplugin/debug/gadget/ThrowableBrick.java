package io.github.thewebcode.yplugin.debug.gadget;

import io.github.thewebcode.yplugin.effect.Effects;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.game.item.ThrowableItem;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class ThrowableBrick extends ThrowableItem {

    private static ThrowableBrick instance = null;

    public static ThrowableBrick getInstance() {
        if (instance == null) {
            instance = new ThrowableBrick();
            Gadgets.registerGadget(instance);
        }
        return instance;
    }

    public ThrowableBrick() {
        super(ItemBuilder.of(Material.BRICK).lore("&eYou'll bash your eye out!"));

        properties().delay(2).action(Action.DELAY);
    }

    @Override
    public void handle(Player holder, Item thrownItem) {
        Effects.explode(thrownItem.getLocation(), 1.0f, false, false);
    }
}
