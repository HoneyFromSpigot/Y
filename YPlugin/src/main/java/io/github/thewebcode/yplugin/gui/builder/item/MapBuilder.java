package io.github.thewebcode.yplugin.gui.builder.item;

import io.github.thewebcode.yplugin.gui.components.exception.GuiException;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapBuilder extends BaseItemBuilder<MapBuilder> {
    private static final Material MAP = Material.MAP;

    MapBuilder() {
        super(new ItemStack(MAP));
    }

    MapBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (itemStack.getType() != MAP) {
            throw new GuiException("MapBuilder requires the material to be a MAP!");
        }
    }

    @NotNull
    @Override
    @Contract("_ -> this")
    public MapBuilder color(@Nullable final Color color) {
        final MapMeta mapMeta = (MapMeta) getMeta();

        mapMeta.setColor(color);
        setMeta(mapMeta);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MapBuilder locationName(@Nullable final String name) {
        final MapMeta mapMeta = (MapMeta) getMeta();

        mapMeta.setLocationName(name);
        setMeta(mapMeta);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MapBuilder scaling(final boolean scaling) {
        final MapMeta mapMeta = (MapMeta) getMeta();

        mapMeta.setScaling(scaling);
        setMeta(mapMeta);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MapBuilder view(@NotNull final MapView view) {
        final MapMeta mapMeta = (MapMeta) getMeta();

        mapMeta.setMapView(view);
        setMeta(mapMeta);
        return this;
    }
}
