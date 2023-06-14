package io.github.thewebcode.yplugin.gui;

import io.github.thewebcode.yplugin.gui.components.InteractionModifier;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StorageGui extends BaseGui{
    public StorageGui(final int rows, @NotNull final String title, @NotNull final Set<InteractionModifier> interactionModifiers) {
        super(rows, title, interactionModifiers);
    }

    @Deprecated
    public StorageGui(final int rows, @NotNull final String title) {
        super(rows, title);
    }

    @Deprecated
    public StorageGui(@NotNull final String title) {
        super(1, title);
    }

    @NotNull
    public Map<@NotNull Integer, @NotNull ItemStack> addItem(@NotNull final ItemStack... items) {
        return Collections.unmodifiableMap(getInventory().addItem(items));
    }

    public Map<@NotNull Integer, @NotNull ItemStack> addItem(@NotNull final List<ItemStack> items) {
        return addItem(items.toArray(new ItemStack[0]));
    }

    @Override
    public void open(@NotNull final HumanEntity player) {
        if (player.isSleeping()) return;
        populateGui();
        player.openInventory(getInventory());
    }
}
