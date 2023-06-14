package io.github.thewebcode.yplugin.gui.builder.item;

import io.github.thewebcode.yplugin.gui.GuiItem;
import io.github.thewebcode.yplugin.gui.components.GuiAction;
import io.github.thewebcode.yplugin.gui.components.exception.GuiException;
import io.github.thewebcode.yplugin.gui.components.util.ItemNbt;
import io.github.thewebcode.yplugin.gui.components.util.Legacy;
import io.github.thewebcode.yplugin.gui.components.util.VersionHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class BaseItemBuilder<B extends BaseItemBuilder> {
    private static final EnumSet<Material> LEATHER_ARMOR = EnumSet.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS
    );

    private static final GsonComponentSerializer GSON = GsonComponentSerializer.gson();
    private static final Field DISPLAY_NAME_FIELD;
    private static final Field LORE_FIELD;

    static {
        try {
            final Class<?> metaClass = VersionHelper.craftClass("inventory.CraftMetaItem");

            DISPLAY_NAME_FIELD = metaClass.getDeclaredField("displayName");
            DISPLAY_NAME_FIELD.setAccessible(true);

            LORE_FIELD = metaClass.getDeclaredField("lore");
            LORE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException exception) {
            exception.printStackTrace();
            throw new GuiException("Could not retrieve displayName nor lore field for ItemBuilder.");
        }
    }

    private ItemStack itemStack;
    private ItemMeta meta;

    protected BaseItemBuilder(@NotNull final ItemStack itemStack) {
        Validate.notNull(itemStack, "Item can't be null!");

        this.itemStack = itemStack;
        meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
    }

    @NotNull
    @Contract("_ -> this")
    public B name(@NotNull final Component name) {
        if (meta == null) return (B) this;

        if (VersionHelper.IS_COMPONENT_LEGACY) {
            meta.setDisplayName(Legacy.SERIALIZER.serialize(name));
            return (B) this;
        }

        try {
            DISPLAY_NAME_FIELD.set(meta, GSON.serialize(name));
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B amount(final int amount) {
        itemStack.setAmount(amount);
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B lore(@Nullable final Component @NotNull ... lore) {
        return lore(Arrays.asList(lore));
    }

    @NotNull
    @Contract("_ -> this")
    public B lore(@NotNull final List<@Nullable Component> lore) {
        if (meta == null) return (B) this;

        if (VersionHelper.IS_COMPONENT_LEGACY) {
            meta.setLore(lore.stream().filter(Objects::nonNull).map(Legacy.SERIALIZER::serialize).collect(Collectors.toList()));
            return (B) this;
        }

        final List<String> jsonLore = lore.stream().filter(Objects::nonNull).map(GSON::serialize).collect(Collectors.toList());

        try {
            LORE_FIELD.set(meta, jsonLore);
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B lore(@NotNull final Consumer<List<@Nullable Component>> lore) {
        if (meta == null) return (B) this;

        List<Component> components;
        if (VersionHelper.IS_COMPONENT_LEGACY) {
            final List<String> stringLore = meta.getLore();
            components = (stringLore == null) ? new ArrayList<>() : stringLore.stream().map(Legacy.SERIALIZER::deserialize).collect(Collectors.toList());
        } else {
            try {
                final List<String> jsonLore = (List<String>) LORE_FIELD.get(meta);
                components = (jsonLore == null) ? new ArrayList<>() : jsonLore.stream().map(GSON::deserialize).collect(Collectors.toList());
            } catch (IllegalAccessException exception) {
                components = new ArrayList<>();
                exception.printStackTrace();
            }
        }

        lore.accept(components);
        return lore(components);
    }

    @NotNull
    @Contract("_, _, _ -> this")
    public B enchant(@NotNull final Enchantment enchantment, final int level, final boolean ignoreLevelRestriction) {
        meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return (B) this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public B enchant(@NotNull final Enchantment enchantment, final int level) {
        return enchant(enchantment, level, true);
    }

    @NotNull
    @Contract("_ -> this")
    public B enchant(@NotNull final Enchantment enchantment) {
        return enchant(enchantment, 1, true);
    }

    @NotNull
    @Contract("_, _ -> this")
    public B enchant(@NotNull final Map<Enchantment, Integer> enchantments, final boolean ignoreLevelRestriction) {
        enchantments.forEach((enchantment, level) -> this.enchant(enchantment, level, ignoreLevelRestriction));
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B enchant(@NotNull final Map<Enchantment, Integer> enchantments) {
        return enchant(enchantments, true);
    }

    @NotNull
    @Contract("_ -> this")
    public B disenchant(@NotNull final Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B flags(@NotNull final ItemFlag... flags) {
        meta.addItemFlags(flags);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B unbreakable() {
        return unbreakable(true);
    }

    @NotNull
    @Contract("_ -> this")
    public B unbreakable(boolean unbreakable) {
        if (VersionHelper.IS_UNBREAKABLE_LEGACY) {
            return setNbt("Unbreakable", unbreakable);
        }

        meta.setUnbreakable(unbreakable);
        return (B) this;
    }

    @NotNull
    @Contract(" -> this")
    public B glow() {
        return glow(true);
    }

    @NotNull
    @Contract("_ -> this")
    public B glow(boolean glow) {
        if (glow) {
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            return (B) this;
        }

        for (final Enchantment enchantment : meta.getEnchants().keySet()) {
            meta.removeEnchant(enchantment);
        }

        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B pdc(@NotNull final Consumer<PersistentDataContainer> consumer) {
        consumer.accept(meta.getPersistentDataContainer());
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B model(final int modelData) {
        if (VersionHelper.IS_CUSTOM_MODEL_DATA) {
            meta.setCustomModelData(modelData);
        }

        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B color(@NotNull final Color color) {
        if (LEATHER_ARMOR.contains(itemStack.getType())) {
            final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) getMeta();

            leatherArmorMeta.setColor(color);
            setMeta(leatherArmorMeta);
        }

        return (B) this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public B setNbt(@NotNull final String key, @NotNull final String value) {
        itemStack.setItemMeta(meta);
        itemStack = ItemNbt.setString(itemStack, key, value);
        meta = itemStack.getItemMeta();
        return (B) this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public B setNbt(@NotNull final String key, final boolean value) {
        itemStack.setItemMeta(meta);
        itemStack = ItemNbt.setBoolean(itemStack, key, value);
        meta = itemStack.getItemMeta();
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B removeNbt(@NotNull final String key) {
        itemStack.setItemMeta(meta);
        itemStack = ItemNbt.removeTag(itemStack, key);
        meta = itemStack.getItemMeta();
        return (B) this;
    }

    @NotNull
    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @NotNull
    @Contract(" -> new")
    public GuiItem asGuiItem() {
        return new GuiItem(build());
    }

    @NotNull
    @Contract("_ -> new")
    public GuiItem asGuiItem(@NotNull final GuiAction<InventoryClickEvent> action) {
        return new GuiItem(build(), action);
    }

    @NotNull
    protected ItemStack getItemStack() {
        return itemStack;
    }

    protected void setItemStack(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    protected ItemMeta getMeta() {
        return meta;
    }

    protected void setMeta(@NotNull final ItemMeta meta) {
        this.meta = meta;
    }

    @Deprecated
    public B setName(@NotNull final String name) {
        getMeta().setDisplayName(name);
        return (B) this;
    }

    @Deprecated
    public B setAmount(final int amount) {
        getItemStack().setAmount(amount);
        return (B) this;
    }

    @Deprecated
    public B addLore(@NotNull final String... lore) {
        return addLore(Arrays.asList(lore));
    }

    @Deprecated
    public B addLore(@NotNull final List<String> lore) {
        final List<String> newLore = getMeta().hasLore() ? getMeta().getLore() : new ArrayList<>();

        newLore.addAll(lore);
        return setLore(newLore);
    }

    @Deprecated
    public B setLore(@NotNull final String... lore) {
        return setLore(Arrays.asList(lore));
    }

    @Deprecated
    public B setLore(@NotNull final List<String> lore) {
        getMeta().setLore(lore);
        return (B) this;
    }

    @Deprecated
    public B addEnchantment(@NotNull final Enchantment enchantment, final int level, final boolean ignoreLevelRestriction) {
        getMeta().addEnchant(enchantment, level, ignoreLevelRestriction);
        return (B) this;
    }

    @Deprecated
    public B addEnchantment(@NotNull final Enchantment enchantment, final int level) {
        return addEnchantment(enchantment, level, true);
    }

    @Deprecated
    public B addEnchantment(@NotNull final Enchantment enchantment) {
        return addEnchantment(enchantment, 1, true);
    }

    @Deprecated
    public B removeEnchantment(@NotNull final Enchantment enchantment) {
        getItemStack().removeEnchantment(enchantment);
        return (B) this;
    }

    @Deprecated
    public B addItemFlags(@NotNull final ItemFlag... flags) {
        getMeta().addItemFlags(flags);
        return (B) this;
    }

    @Deprecated
    public B setUnbreakable(boolean unbreakable) {
        return unbreakable(unbreakable);
    }
}
