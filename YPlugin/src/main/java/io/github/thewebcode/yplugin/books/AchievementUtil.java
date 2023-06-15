package io.github.thewebcode.yplugin.books;

import java.util.EnumMap;
import java.util.Map;

public final class AchievementUtil {
    private static final Map<Advancement, String> achievements = new EnumMap<Advancement, String>(Advancement.class) {{
        put(Advancement.OPEN_INVENTORY, "openInventory");
        put(Advancement.MINE_WOOD, "mineWood");
        put(Advancement.BUILD_WORKBENCH, "buildWorkBench");
        put(Advancement.BUILD_PICKAXE, "buildPickaxe");
        put(Advancement.BUILD_FURNACE, "buildFurnace");
        put(Advancement.ACQUIRE_IRON, "aquireIron");
        put(Advancement.BUILD_HOE, "buildHoe");
        put(Advancement.BAKE_CAKE,"bakeCake");
        put(Advancement.BUILD_BETTER_PICKAXE,"buildBetterPickaxe");
        put(Advancement.MAKE_BREAD, "makeBread");
        put(Advancement.COOK_FISH,"cookFish");
        put(Advancement.ON_A_RAIL,"onARail");
        put(Advancement.BUILD_SWORD,"buildSword");
        put(Advancement.KILL_ENEMY,"killEnemy");
        put(Advancement.KILL_COW,"killCow");
        put(Advancement.FLY_PIG,"flyPig");
        put(Advancement.SNIPE_SKELETON,"snipeSkeleton");
        put(Advancement.GET_DIAMONDS,"diamonds");
        put(Advancement.NETHER_PORTAL,"portal");
        put(Advancement.GHAST_RETURN,"ghast");
        put(Advancement.GET_BLAZE_ROD,"blazerod");
        put(Advancement.BREW_POTION,"potion");
        put(Advancement.END_PORTAL,"thEnd");
        put(Advancement.THE_END,"theEnd2");
        put(Advancement.ENCHANTMENTS,"enchantments");
        put(Advancement.OVERKILL,"overkill");
        put(Advancement.BOOKCASE,"bookacase");
        put(Advancement.EXPLORE_ALL_BIOMES,"exploreAllBiomes");
        put(Advancement.SPAWN_WITHER,"spawnWither");
        put(Advancement.KILL_WITHER,"killWither");
        put(Advancement.FULL_BEACON,"fullBeacon");
        put(Advancement.BREED_COW,"breedCow");
        put(Advancement.DIAMONDS_TO_YOU,"diamondsToYou");
        put(Advancement.OVERPOWERED, "overpowered");
    }};


    public static String toId(Advancement achievement) {
        return achievements.get(achievement);
    }

    private AchievementUtil(){}

    public static enum Advancement{
        OPEN_INVENTORY,
        MINE_WOOD,
        BUILD_WORKBENCH,
        BUILD_PICKAXE,
        BUILD_FURNACE,
        ACQUIRE_IRON,
        BUILD_HOE,
        MAKE_BREAD,
        BAKE_CAKE,
        BUILD_BETTER_PICKAXE,
        COOK_FISH,
        ON_A_RAIL,
        BUILD_SWORD,
        KILL_ENEMY,
        KILL_COW,
        FLY_PIG,
        SNIPE_SKELETON,
        GET_DIAMONDS,
        NETHER_PORTAL,
        GHAST_RETURN,
        GET_BLAZE_ROD,
        BREW_POTION,
        END_PORTAL,
        THE_END,
        ENCHANTMENTS,
        OVERKILL,
        BOOKCASE,
        EXPLORE_ALL_BIOMES,
        SPAWN_WITHER,
        KILL_WITHER,
        FULL_BEACON,
        BREED_COW,
        DIAMONDS_TO_YOU,
        OVERPOWERED
    }
}
