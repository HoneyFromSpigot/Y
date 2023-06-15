package io.github.thewebcode.yplugin.game.gadget;

import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.yml.Path;
import io.github.thewebcode.yplugin.yml.YamlConfig;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

import java.io.File;

;

@ToString(of = {"durability", "isBreakable", "isDroppable"})
public class GadgetProperties extends YamlConfig {
    @Path("durability")
    private int durability;

    @Path("breakable")
    private boolean isBreakable;

    @Path("droppable")
    private boolean isDroppable = false;

    @Path("offhand-allowed")
    private boolean offHandEquipable = true;

    public GadgetProperties() {

    }

    public GadgetProperties(File file) {
        super(file);
    }

    public GadgetProperties(int durability,boolean isBreakable,boolean isDroppable,boolean offHandEquipable) {
        this.durability = durability;
        this.isBreakable = isBreakable;
        this.isDroppable = isDroppable;
        this.offHandEquipable = offHandEquipable;
    }

    public GadgetProperties breakable(boolean canBreak) {
        this.isBreakable = canBreak;
        return this;
    }

    public GadgetProperties droppable(boolean canDrop) {
        this.isDroppable = canDrop;
        return this;
    }

    public GadgetProperties durability(int uses) {
        this.durability = uses;
        return this;
    }

    public GadgetProperties durability(ItemStack item) {
        if (Items.isWeapon(item) || Items.isTool(item)) {
            return durability(item.getDurability());
        } else {
            durability = -1;
        }

        return this;
    }

    public GadgetProperties offHandEquippable(boolean equip) {
        this.offHandEquipable = equip;
        return this;
    }

    public boolean isBreakable() {
        return isBreakable || durability == -1;
    }

    public boolean isDroppable() {
        return isDroppable;
    }

    public int getDurability() {
        return durability;
    }

    public boolean isOffhandEquippable() {
        return offHandEquipable;
    }
}
