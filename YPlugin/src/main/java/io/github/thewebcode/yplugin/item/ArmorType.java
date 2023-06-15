package io.github.thewebcode.yplugin.item;

import com.google.common.collect.Sets;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum ArmorType {
    LEATHER(Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET),
    IRON(Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET),
    GOLD(Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET),
    CHAIN(Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET),
    DIAMOND(Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET);

    private static Map<ArmorType, Set<Material>> armorTypes = new HashMap<>();

    static {
        for (ArmorType type : ArmorType.values()) {
            armorTypes.put(type, Sets.newHashSet(type.armor));
        }
    }

    private Material[] armor;

    ArmorType(Material... type) {
        this.armor = type;
    }

    public static ArmorType getArmorType(Material mat) {
        for (Map.Entry<ArmorType, Set<Material>> armorTypeEntry : armorTypes.entrySet()) {
            if (armorTypeEntry.getValue().contains(mat)) {
                return armorTypeEntry.getKey();
            }
        }
        return null;
    }
}
