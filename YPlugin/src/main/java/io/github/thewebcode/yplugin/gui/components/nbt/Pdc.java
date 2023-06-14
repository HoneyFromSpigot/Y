package io.github.thewebcode.yplugin.gui.components.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Pdc implements NbtWrapper {
    private static final Plugin PLUGIN = JavaPlugin.getProvidingPlugin(Pdc.class);

    @Override
    public ItemStack setString(@NotNull final ItemStack itemStack, final String key, final String value) {
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        meta.getPersistentDataContainer().set(new NamespacedKey(PLUGIN, key), PersistentDataType.STRING, value);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public ItemStack removeTag(@NotNull final ItemStack itemStack, final String key) {
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        meta.getPersistentDataContainer().remove(new NamespacedKey(PLUGIN, key));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public ItemStack setBoolean(@NotNull final ItemStack itemStack, final String key, final boolean value) {
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        meta.getPersistentDataContainer().set(new NamespacedKey(PLUGIN, key), PersistentDataType.BYTE, value ? (byte) 1 : 0);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Nullable
    @Override
    public String getString(@NotNull final ItemStack itemStack, final String key) {
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return null;
        return meta.getPersistentDataContainer().get(new NamespacedKey(PLUGIN, key), PersistentDataType.STRING);
    }
}
