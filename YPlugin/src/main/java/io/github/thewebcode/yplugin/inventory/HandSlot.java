package io.github.thewebcode.yplugin.inventory;

import org.bukkit.inventory.EquipmentSlot;

public enum HandSlot {
    MAIN_HAND,
    OFF_HAND;

    public static HandSlot getSlot(EquipmentSlot slot) {
        switch (slot) {
            case HAND:
                return MAIN_HAND;
            case OFF_HAND:
                return OFF_HAND;
            case FEET:
            case LEGS:
            case CHEST:
            case HEAD:
                return null;
        }

        return null;
    }
}
