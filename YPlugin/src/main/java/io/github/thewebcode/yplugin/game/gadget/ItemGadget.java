package io.github.thewebcode.yplugin.game.gadget;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ItemGadget implements Gadget {

    private ItemStack gadgetItem;

    private GadgetProperties properties = new GadgetProperties();

    private int id = 0;

    public ItemGadget() {
        id = Gadgets.getFirstFreeId();
        Chat.debug("Registered Gadget [NO ITEM] under ID " + id);
    }

    public ItemGadget(ItemBuilder builder) {
        ItemStack item = builder.item();
        setItem(item);

        id = Gadgets.getFirstFreeId();
        Chat.debug("Registered Gadget " + Items.getName(getItem()) + " with  ID " + id);

    }

    public ItemGadget(ItemStack item) {
        id = Gadgets.getFirstFreeId();
        setItem(item);
        Chat.debug("Registered Gadget " + Items.getName(getItem()) + " with  ID " + id);

    }

    public ItemStack getItem() {
        return gadgetItem;
    }

    public void setItem(ItemStack item) {
        this.gadgetItem = item.clone();
        properties.durability(item);
    }

    public void giveTo(Player player) {
        Players.giveItem(player, getItem());
    }

    public abstract void perform(Player holder);

    public void onDrop(Player player, Item dropped) {

    }

    @Override
    public GadgetProperties properties() {
        return properties;
    }

    @Override
    public int id() {
        return id;
    }

}
