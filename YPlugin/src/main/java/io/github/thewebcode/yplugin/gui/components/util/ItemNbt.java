package io.github.thewebcode.yplugin.gui.components.util;

import io.github.thewebcode.yplugin.gui.components.nbt.LegacyNbt;
import io.github.thewebcode.yplugin.gui.components.nbt.NbtWrapper;
import io.github.thewebcode.yplugin.gui.components.nbt.Pdc;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemNbt {
    private static final NbtWrapper nbt = selectNbt();

    public static ItemStack setString(@NotNull final ItemStack itemStack, @NotNull final String key, @NotNull final String value) {
        return nbt.setString(itemStack, key, value);
    }

    public static String getString(@NotNull final ItemStack itemStack, @NotNull final String key) {
        return nbt.getString(itemStack, key);
    }

    public static ItemStack setBoolean(@NotNull final ItemStack itemStack, @NotNull final String key, final boolean value) {
        return nbt.setBoolean(itemStack, key, value);
    }

    public static ItemStack removeTag(@NotNull final ItemStack itemStack, @NotNull final String key) {
        return nbt.removeTag(itemStack, key);
    }

    private static NbtWrapper selectNbt() {
        if (VersionHelper.IS_PDC_VERSION) return new Pdc();
        return new LegacyNbt();
    }
}
