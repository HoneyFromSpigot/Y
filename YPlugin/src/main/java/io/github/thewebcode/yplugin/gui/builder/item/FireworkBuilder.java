package io.github.thewebcode.yplugin.gui.builder.item;

import io.github.thewebcode.yplugin.gui.components.exception.GuiException;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class FireworkBuilder extends BaseItemBuilder<FireworkBuilder>{
    private static final Material STAR = Material.FIREWORK_STAR;
    private static final Material ROCKET = Material.FIREWORK_ROCKET;

    FireworkBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
        if (itemStack.getType() != STAR && itemStack.getType() != ROCKET) {
            throw new GuiException("FireworkBuilder requires the material to be a FIREWORK_STAR/FIREWORK_ROCKET!");
        }
    }

    @NotNull
    @Contract("_ -> this")
    public FireworkBuilder effect(@NotNull final FireworkEffect... effects) {
        return effect(Arrays.asList(effects));
    }

    @NotNull
    @Contract("_ -> this")
    public FireworkBuilder effect(@NotNull final List<FireworkEffect> effects) {
        if (effects.isEmpty()) {
            return this;
        }

        if (getItemStack().getType() == STAR) {
            final FireworkEffectMeta effectMeta = (FireworkEffectMeta) getMeta();

            effectMeta.setEffect(effects.get(0));
            setMeta(effectMeta);
            return this;
        }

        final FireworkMeta fireworkMeta = (FireworkMeta) getMeta();

        fireworkMeta.addEffects(effects);
        setMeta(fireworkMeta);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public FireworkBuilder power(final int power) {
        if (getItemStack().getType() == ROCKET) {
            final FireworkMeta fireworkMeta = (FireworkMeta) getMeta();

            fireworkMeta.setPower(power);
            setMeta(fireworkMeta);
        }

        return this;
    }
}
