package io.github.thewebcode.yplugin.gui;

import io.github.thewebcode.yplugin.gui.components.GuiAction;
import io.github.thewebcode.yplugin.gui.components.util.ItemNbt;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GuiItem {
    private GuiAction<InventoryClickEvent> action;

    private ItemStack itemStack;

    private final UUID uuid = UUID.randomUUID();

    public GuiItem(@NotNull final ItemStack itemStack, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        Validate.notNull(itemStack, "The ItemStack for the GUI Item cannot be null!");

        this.action = action;
        this.itemStack = ItemNbt.setString(itemStack, "mf-gui", uuid.toString());
    }

    public GuiItem(@NotNull final ItemStack itemStack) {
        this(itemStack, null);
    }

    public GuiItem(@NotNull final Material material) {
        this(new ItemStack(material), null);
    }

    public GuiItem(@NotNull final Material material, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this(new ItemStack(material), action);
    }

    public void setItemStack(@NotNull final ItemStack itemStack) {
        Validate.notNull(itemStack, "The ItemStack for the GUI Item cannot be null!");
        this.itemStack = ItemNbt.setString(itemStack, "mf-gui", uuid.toString());
    }

    public void setAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this.action = action;
    }

    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    @NotNull
    UUID getUuid() {
        return uuid;
    }

    @Nullable
    GuiAction<InventoryClickEvent> getAction() {
        return action;
    }
}
