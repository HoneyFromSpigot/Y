package io.github.thewebcode.yplugin.potion;

import io.github.thewebcode.yplugin.utilities.NumberUtil;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CyclicPotion {

    private int durMin;
    private int durMax;
    private int ampMin;
    private int ampMax;

    private PotionEffectType type;

    private PotionEffect effect;

    public CyclicPotion(PotionEffectType type, int durationMin, int durationMax, int amplifierMin, int amplifierMax) {
        this.type = type;
        this.durMin = durationMin;
        this.durMax = durationMax;
        this.ampMin = amplifierMin;
        this.ampMax = amplifierMax;
    }

    public CyclicPotion effect(PotionEffectType type) {
        this.type = type;
        return this;
    }

    public CyclicPotion randomize() {
        effect = Potions.getPotionEffect(type, NumberUtil.getRandomInRange(ampMin, ampMax), NumberUtil.getRandomInRange(durMin, durMax));
        return this;
    }

    public PotionEffect effect() {
        return effect;
    }
}
