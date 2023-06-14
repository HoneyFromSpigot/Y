package io.github.thewebcode.yplugin.gui.builder.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.thewebcode.yplugin.gui.components.util.SkullUtil;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.UUID;

public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {
    ItemBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    @NotNull
    @Contract("_ -> new")
    public static ItemBuilder from(@NotNull final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }


    @NotNull
    @Contract("_ -> new")
    public static ItemBuilder from(@NotNull final Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    @NotNull
    @Contract(" -> new")
    public static BannerBuilder banner() {
        return new BannerBuilder();
    }

    @NotNull
    @Contract("_ -> new")
    public static BannerBuilder banner(@NotNull final ItemStack itemStack) {
        return new BannerBuilder(itemStack);
    }

    @NotNull
    @Contract("_ -> new")
    public static BookBuilder book(@NotNull final ItemStack itemStack) {
        return new BookBuilder(itemStack);
    }

    @NotNull
    @Contract(" -> new")
    public static FireworkBuilder firework() {
        return new FireworkBuilder(new ItemStack(Material.FIREWORK_ROCKET));
    }

    @NotNull
    @Contract("_ -> new")
    public static FireworkBuilder firework(@NotNull final ItemStack itemStack) {
        return new FireworkBuilder(itemStack);
    }

    @NotNull
    @Contract(" -> new")
    public static MapBuilder map() {
        return new MapBuilder();
    }

    @NotNull
    @Contract("_ -> new")
    public static MapBuilder map(@NotNull final ItemStack itemStack) {
        return new MapBuilder(itemStack);
    }

    @NotNull
    @Contract(" -> new")
    public static SkullBuilder skull() {
        return new SkullBuilder();
    }

    @NotNull
    @Contract("_ -> new")
    public static SkullBuilder skull(@NotNull final ItemStack itemStack) {
        return new SkullBuilder(itemStack);
    }

    @NotNull
    @Contract(" -> new")
    public static FireworkBuilder star() {
        return new FireworkBuilder(new ItemStack(Material.FIREWORK_STAR));
    }

    @NotNull
    @Contract("_ -> new")
    public static FireworkBuilder star(@NotNull final ItemStack itemStack) {
        return new FireworkBuilder(itemStack);
    }

    @Deprecated
    public ItemBuilder setSkullTexture(@NotNull final String texture) {
        if (!SkullUtil.isPlayerSkull(getItemStack())) return this;

        final SkullMeta skullMeta = (SkullMeta) getMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        final Field profileField;

        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        setMeta(skullMeta);
        return this;
    }

    @Deprecated
    public ItemBuilder setSkullOwner(@NotNull final OfflinePlayer player) {
        if (!SkullUtil.isPlayerSkull(getItemStack())) return this;

        final SkullMeta skullMeta = (SkullMeta) getMeta();
        skullMeta.setOwningPlayer(player);

        setMeta(skullMeta);
        return this;
    }
}
