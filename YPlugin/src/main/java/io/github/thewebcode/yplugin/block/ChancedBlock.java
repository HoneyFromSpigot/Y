package io.github.thewebcode.yplugin.block;

import io.github.thewebcode.yplugin.utilities.NumberUtil;
import org.bukkit.Material;

public class ChancedBlock {
    private Material mat;
    private byte data;
    private int chance;

    public static ChancedBlock of(Material mat, int id, int chance) {
        return new ChancedBlock(mat, id, chance);
    }

    public ChancedBlock() {

    }

    public ChancedBlock(Material material, int data, int chance) {
        this.mat = material;
        this.data = (byte) data;
        this.chance = chance;
    }

    public ChancedBlock data(int data) {
        this.data = (byte) data;
        return this;
    }

    public ChancedBlock data(Material mat, int data) {
        this.mat = mat;
        return data(data);
    }

    public ChancedBlock chance(int chance) {
        this.chance = chance;
        return this;
    }

    public boolean pass() {
        return NumberUtil.percentCheck(chance);
    }

    public Material getMaterial() {
        return mat;
    }

    public byte getData() {
        return data;
    }
}
