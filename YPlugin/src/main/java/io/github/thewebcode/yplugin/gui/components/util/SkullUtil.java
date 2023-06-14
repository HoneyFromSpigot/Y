package io.github.thewebcode.yplugin.gui.components.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SkullUtil {
    private static final Material SKULL = getSkullMaterial();
    private static Material getSkullMaterial() {
        if (VersionHelper.IS_ITEM_LEGACY) {
            return Material.valueOf("SKULL_ITEM");
        }

        return Material.PLAYER_HEAD;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack skull() {
        return VersionHelper.IS_ITEM_LEGACY ? new ItemStack(SKULL, 1, (short) 3) : new ItemStack(SKULL);
    }

    @SuppressWarnings("deprecation")
    public static boolean isPlayerSkull(@NotNull final ItemStack item) {
        if (VersionHelper.IS_ITEM_LEGACY) {
            return item.getType() == SKULL && item.getDurability() == (short) 3;
        }

        return item.getType() == SKULL;
    }
}
