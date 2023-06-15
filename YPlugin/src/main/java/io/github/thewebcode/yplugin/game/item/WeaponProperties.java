package io.github.thewebcode.yplugin.game.item;

import io.github.thewebcode.yplugin.game.gadget.GadgetProperties;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import io.github.thewebcode.yplugin.yml.Path;
import lombok.ToString;

import java.io.File;

@ToString(of = {"damageMin", "damageMax"}, callSuper = true)
public class WeaponProperties extends GadgetProperties {
    @Path("damage-min")
    private double damageMin = 0;

    @Path("damage-max")
    private double damageMax = 0;

    public WeaponProperties() {

    }

    public WeaponProperties(File file) {
        super(file);
    }

    public WeaponProperties damage(double min, double max) {
        this.damageMin = min;
        this.damageMax = max;
        return this;
    }

    public double getDamage() {
        return NumberUtil.randomDouble(damageMin, damageMax);
    }

    public double getMinDamage() {
        return damageMin;
    }

    public double getMaxDamage() {
        return damageMax;
    }

    public boolean hasDamageRange() {
        return damageMin > 0 && damageMax > 0;
    }
}
