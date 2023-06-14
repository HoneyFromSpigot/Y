package io.github.thewebcode.yplugin.gui.components.nbt;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NbtWrapper {
    ItemStack setString(@NotNull final ItemStack itemStack, final String key, final String value);

    ItemStack removeTag(@NotNull final ItemStack itemStack, final String key);

    ItemStack setBoolean(@NotNull final ItemStack itemStack, final String key, final boolean value);

    @Nullable
    String getString(@NotNull final ItemStack itemStack, final String key);
}
