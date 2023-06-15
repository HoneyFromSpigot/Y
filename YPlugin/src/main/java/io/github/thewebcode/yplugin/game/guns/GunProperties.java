package io.github.thewebcode.yplugin.game.guns;

import io.github.thewebcode.yplugin.game.item.WeaponProperties;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;

import io.github.thewebcode.yplugin.yml.Path;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class GunProperties extends WeaponProperties {
    @Path("clip-size")
    public int clipSize = 20;

    @Path("shot-delay")
    public long shotDelay = 1500l;

    @Path("reload-speed")
    public int reloadSpeed = 5;

    @Path("reload-message")
    public boolean reloadMessage = true;

    @Path("display-ammo")
    public boolean displayAmmo = true;

    @Path("range")
    public int range = 100;

    @Path("ammunition")
    private ItemStack ammunition = Items.makeItem(Material.FLINT);

    @Path("rounds-per-shot")
    public int roundsPerShot = 1;

    @Path("cluster-shot")
    public boolean clusterShot = false;

    @Path("take-ammunition-on-fire")
    public boolean takeAmmunition = true;

    private Gun parent;

    public GunProperties() {
        droppable(false);
        breakable(false);
    }

    public GunProperties(Gun parent) {
        this();

        this.parent = parent;
    }

    public GunProperties clipSize(int size) {
        this.clipSize = size;
        return this;
    }

    public GunProperties reloadTicks(int amt) {
        reloadSpeed = amt;
        return this;
    }


    public GunProperties reloadSpeed(int amount, TimeType type) {
        return reloadTicks((int) TimeHandler.getTimeInTicks(amount, type));
    }

    public GunProperties reloadSpeed(int seconds) {
        return reloadSpeed(seconds, TimeType.SECOND);
    }

    public GunProperties roundsPerShot(int amount) {
        this.roundsPerShot = amount;
        return this;
    }

    public GunProperties range(int range) {
        this.range = range;
        return this;
    }

    public GunProperties ammunition(ItemStack item) {
        this.ammunition = item.clone();
        return this;
    }

    public GunProperties ammunition(ItemBuilder builder) {
        ammunition(builder.item());
        return this;
    }

    public GunProperties shotDelay(long millis) {
        this.shotDelay = millis;
        return this;
    }

    public GunProperties clusterShot(boolean val) {
        this.clusterShot = val;
        return this;
    }

    public GunProperties reloadMessage(boolean val) {
        this.reloadMessage = val;
        return this;
    }

    public GunProperties displayAmmo(boolean val) {
        this.displayAmmo = val;
        return this;
    }

    public GunProperties takeAmmunition(boolean val) {
        this.takeAmmunition = val;
        return this;
    }

    public GunProperties damage(double amt) {
        damage(amt, amt);
        return this;
    }

    public ItemStack ammunition() {
        return ammunition;
    }

    public Gun parent() {
        return parent;
    }
}
