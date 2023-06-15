package io.github.thewebcode.yplugin.game.item;

import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import io.github.thewebcode.yplugin.yml.Path;
import io.github.thewebcode.yplugin.yml.YamlConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class RewardData extends YamlConfig {
    @Path("min-amount")
    private int min = 1;

    @Path("max-amount")
    private int max = 1;

    @Path("spawn-chance")
    private int spawnChance = 100;

    @Path("item")
    private ItemStack itemStack = new ItemStack(Items.makeItem(Material.GOLDEN_BOOTS));

    public RewardData() {

    }

    public RewardData(File file) {
    }

    public RewardData(int min, int max, int spawnChance, ItemStack itemStack) {
        this.min = min;
        this.max = max;
        this.spawnChance = spawnChance;
        this.itemStack = itemStack;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSpawnChance() {
        return spawnChance;
    }

    public void setSpawnChance(int spawnChance) {
        this.spawnChance = spawnChance;
    }

    public ItemStack generateRewardItem() {
        if (!NumberUtil.percentCheck(getSpawnChance())) {
            return null;
        }

        ItemStack item = itemStack.clone();
        if (getMin() >= getMax()) {
            item.setAmount(getMax());
        } else {
            item.setAmount(NumberUtil.getRandomInRange(getMin(), getMax()));
        }
        return item;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack item) {
        itemStack = item.clone();
    }
}