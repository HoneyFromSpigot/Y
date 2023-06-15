package io.github.thewebcode.yplugin.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.effect.Effects;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.location.Locations;
import io.github.thewebcode.yplugin.plugin.Plugins;
import io.github.thewebcode.yplugin.threading.tasks.BlockRegenThread;
import io.github.thewebcode.yplugin.threading.tasks.BlocksRegenThread;
import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;
import io.github.thewebcode.yplugin.utilities.ListUtils;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.stream.Collectors;

public class Blocks {

    private static final Random random = new Random();

    public static final long BLOCK_REGEN_DELAY = TimeHandler.getTimeInTicks(8, TimeType.SECOND);

    private static final Set<Material> HOLLOW_MATERIALS = new HashSet<>();
    public static final HashSet<Material> TRANSPARENT_MATERIALS = new HashSet<>();

    static {
        HOLLOW_MATERIALS.add(Material.AIR);
        HOLLOW_MATERIALS.add(Material.OAK_SAPLING);
        HOLLOW_MATERIALS.add(Material.ACACIA_SAPLING);
        if (Plugins.getBukkitVersion().contains("1.14")) {
            HOLLOW_MATERIALS.add(Material.BAMBOO_SAPLING);
        }
        HOLLOW_MATERIALS.add(Material.BIRCH_SAPLING);
        HOLLOW_MATERIALS.add(Material.DARK_OAK_SAPLING);
        HOLLOW_MATERIALS.add(Material.JUNGLE_SAPLING);
        HOLLOW_MATERIALS.add(Material.SPRUCE_SAPLING);
        HOLLOW_MATERIALS.add(Material.TALL_GRASS);
        HOLLOW_MATERIALS.add(Material.TALL_SEAGRASS);
        HOLLOW_MATERIALS.add(Material.SEAGRASS);
        HOLLOW_MATERIALS.add(Material.GRASS);
        HOLLOW_MATERIALS.add(Material.CHORUS_FLOWER);
        HOLLOW_MATERIALS.add(Material.DANDELION);
        HOLLOW_MATERIALS.add(Material.AZURE_BLUET);
        HOLLOW_MATERIALS.add(Material.LEGACY_YELLOW_FLOWER);
        HOLLOW_MATERIALS.add(Material.LEGACY_RED_ROSE);
        HOLLOW_MATERIALS.add(Material.ROSE_BUSH);
        HOLLOW_MATERIALS.add(Material.BEETROOT_SEEDS);
        HOLLOW_MATERIALS.add(Material.MELON_SEEDS);
        HOLLOW_MATERIALS.add(Material.PUMPKIN_SEEDS);
        HOLLOW_MATERIALS.add(Material.WHEAT_SEEDS);
        HOLLOW_MATERIALS.add(Material.ACACIA_SIGN);
        HOLLOW_MATERIALS.add(Material.BIRCH_SIGN);
        HOLLOW_MATERIALS.add(Material.DARK_OAK_SIGN);
        HOLLOW_MATERIALS.add(Material.JUNGLE_SIGN);
        HOLLOW_MATERIALS.add(Material.OAK_SIGN);
        HOLLOW_MATERIALS.add(Material.SPRUCE_SIGN);
        HOLLOW_MATERIALS.add(Material.ACACIA_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.BIRCH_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.OAK_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.DARK_OAK_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.JUNGLE_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.SPRUCE_WALL_SIGN);


        HOLLOW_MATERIALS.add(Material.POWERED_RAIL);
        HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL);
        HOLLOW_MATERIALS.add(Material.DEAD_BUSH);
        HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.RED_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.TORCH);
        HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE);
        HOLLOW_MATERIALS.add(Material.LADDER);
        HOLLOW_MATERIALS.add(Material.RAIL);
        HOLLOW_MATERIALS.add(Material.LEVER);
        HOLLOW_MATERIALS.add(Material.STONE_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.ACACIA_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.BIRCH_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.JUNGLE_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.OAK_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.DARK_OAK_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);

        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH);
        HOLLOW_MATERIALS.add(Material.REDSTONE_WALL_TORCH);

        HOLLOW_MATERIALS.add(Material.SUGAR_CANE);

        HOLLOW_MATERIALS.add(Material.LEGACY_DIODE_BLOCK_ON);
        HOLLOW_MATERIALS.add(Material.LEGACY_DIODE_BLOCK_OFF);


        HOLLOW_MATERIALS.add(Material.STONE_BUTTON);
        HOLLOW_MATERIALS.add(Material.SNOW);
        HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM);
        HOLLOW_MATERIALS.add(Material.MELON_STEM);
        HOLLOW_MATERIALS.add(Material.VINE);
        HOLLOW_MATERIALS.add(Material.LILY_PAD);
        HOLLOW_MATERIALS.add(Material.LILY_OF_THE_VALLEY);
        HOLLOW_MATERIALS.add(Material.NETHER_WART_BLOCK);

        try {
            HOLLOW_MATERIALS.add(Material.BLACK_CARPET);
            HOLLOW_MATERIALS.add(Material.BLUE_CARPET);
            HOLLOW_MATERIALS.add(Material.RED_CARPET);
            HOLLOW_MATERIALS.add(Material.CYAN_CARPET);
            HOLLOW_MATERIALS.add(Material.BROWN_CARPET);
            HOLLOW_MATERIALS.add(Material.GRAY_CARPET);
            HOLLOW_MATERIALS.add(Material.GREEN_CARPET);
            HOLLOW_MATERIALS.add(Material.LIGHT_BLUE_CARPET);
            HOLLOW_MATERIALS.add(Material.LIGHT_GRAY_CARPET);
            HOLLOW_MATERIALS.add(Material.LIME_CARPET);
            HOLLOW_MATERIALS.add(Material.MAGENTA_CARPET);
            HOLLOW_MATERIALS.add(Material.ORANGE_CARPET);
            HOLLOW_MATERIALS.add(Material.YELLOW_CARPET);
            HOLLOW_MATERIALS.add(Material.PINK_CARPET);
            HOLLOW_MATERIALS.add(Material.PURPLE_CARPET);
            HOLLOW_MATERIALS.add(Material.WHITE_CARPET);
        } catch (NoSuchFieldError e) {
            Chat.debug(Messages.OUTDATED_VERSION);
        }
        TRANSPARENT_MATERIALS.addAll(HOLLOW_MATERIALS);
        TRANSPARENT_MATERIALS.add(Material.WATER);
        TRANSPARENT_MATERIALS.add(Material.LEGACY_STATIONARY_WATER);
    }

    public static List<Block> getBlocks(List<Location> locs) {
        return locs.stream().map(Location::getBlock).collect(Collectors.toList());
    }

    public static int getBlockTypeDistance(Location loc, Material searchMaterial, int depth) {
        World world = loc.getWorld();
        double baseX = loc.getX();
        double baseY = loc.getY();
        double baseZ = loc.getZ();
        for (int depthLevel = 0; depthLevel < depth; ++depthLevel) {

            int deepZ;
            int deepY;
            for (deepZ = -depthLevel; deepZ <= depthLevel; ++deepZ) {
                for (deepY = -depthLevel; deepY <= depthLevel; ++deepY) {

                    Block blockAtPlus = getBlockAt(Locations.getLocation(world, baseX + deepZ, baseY + depthLevel, baseZ + deepY));
                    Block blockAtMinus = getBlockAt(Locations.getLocation(world, baseX + deepZ, baseY - depthLevel, baseZ + deepY));

                    if (blockAtPlus.getType() == searchMaterial || blockAtMinus.getType() == searchMaterial) {
                        return depthLevel;
                    }
                }
            }

            for (deepZ = -depthLevel; deepZ <= depthLevel; ++deepZ) {
                for (deepY = (-depthLevel + 1); deepY <= (depthLevel - 1); ++deepY) {
                    Block blockAtPlus = getBlockAt(Locations.getLocation(world, baseX + deepZ, baseY + deepY, baseZ + depthLevel));
                    Block blockAtMinus = getBlockAt(Locations.getLocation(world, baseX + deepZ, baseY + deepY, baseZ - depthLevel));

                    if (blockAtPlus.getType() == searchMaterial || blockAtMinus.getType() == searchMaterial) {
                        return depthLevel;
                    }
                }
            }

            for (deepZ = (-depthLevel + 1); deepZ < (depthLevel - 1); ++deepZ) {
                for (deepY = (-depthLevel + 1); deepY <= (depthLevel - 1); ++deepY) {
                    Block blockAtPlus = getBlockAt(Locations.getLocation(world, baseX + depthLevel, baseY + deepY, baseZ + deepZ));
                    Block blockAtMinus = getBlockAt(Locations.getLocation(world, baseX - depthLevel, baseY + deepY, baseZ + deepZ));

                    if (blockAtPlus.getType() == searchMaterial || blockAtMinus.getType() == searchMaterial) {
                        return depthLevel;
                    }
                }
            }
        }

        return -1;
    }


    public static Material getBlockMaterial(Block block) {
        Material itemMaterial = block.getType();
        switch (itemMaterial) {
            case REDSTONE_WIRE:
                itemMaterial = Material.REDSTONE;
                break;
            case FIRE:
                itemMaterial = Material.AIR;
                break;
            case PUMPKIN_STEM:
                itemMaterial = Material.PUMPKIN_SEEDS;
                break;
            case MELON_STEM:
                itemMaterial = Material.MELON_SEEDS;
                break;
        }
        return itemMaterial;
    }

    public static int getBlockId(Block block) {
        return getBlockId(block, true);
    }

    public static int getBlockId(Block block, boolean itemsId) {
        return itemsId ? getBlockMaterial(block).getId() : block.getType().getId();
    }

    public static boolean isSolid(Location loc) {
        return getBlockAt(loc).getType().isSolid();
    }

    public static boolean isHollowBlock(Block block) {
        return HOLLOW_MATERIALS.contains(getBlockId(block, false));
    }

    public static boolean isTransparentBlock(Block block) {
        return TRANSPARENT_MATERIALS.contains((byte) getBlockId(block, false));
    }

    public static boolean isBlockAboveAir(Block block) {
        return isHollowBlock(getBlockAbove(block));
    }

    public static boolean isBlockDamaging(Block block) {
        Block blockBelow = getBlockBelow(block);
        switch (blockBelow.getType()) {
            case LAVA:
            case FIRE:
                return true;
            default:
                return !HOLLOW_MATERIALS.contains(getBlockId(block, false)) || !HOLLOW_MATERIALS.contains(getBlockId(getBlockAbove(block), false));
        }
    }

    public static Block getBlockAt(Location blockLocation) {
        return blockLocation.getWorld().getBlockAt(blockLocation);
    }

    public static boolean breakBlockAt(Location blockLocation, boolean natural) {
        return breakBlockAt(blockLocation, natural, false);
    }

    public static boolean breakBlockAt(Location blockLocation, boolean natural, boolean playeffect) {
        Block block = getBlockAt(blockLocation);
        if (block != null) {
            return breakBlock(block, natural, playeffect);
        }
        return false;
    }

    public static boolean breakBlock(Block block, boolean natural) {
        return natural ? block.breakNaturally() : breakBlock(block, false, false);
    }

    public static boolean breakBlock(Block block, boolean natural, boolean playEffect) {
        if (natural) {
            return block.breakNaturally();
        } else {
            setBlock(block, Material.AIR);
            if (playEffect) {
                Effects.playBlockBreakEffect(block.getLocation(), 4, block.getType());
            }
            return true;
        }
    }

    @Deprecated
    public static void breakTreeSafely(Player player, Block block, boolean leaves, boolean dropItems) {
        Material type = block.getType();
        ItemStack drop = null;
    }

    public static void scheduleBlockRegen(Block block, boolean effect) {
        scheduleBlockRegen(block, effect, BLOCK_REGEN_DELAY);
    }

    public static void scheduleBlockRegen(Block block, boolean effect, long delay) {
        BlockData blockData = new BlockData(block);
        YPlugin.getInstance().getThreadManager().runTaskLater(new BlockRegenThread(blockData, effect), delay);
    }

    public static void scheduleBlockRegen(List<Block> blocks, boolean effect, int secondsDelay) {
        YPlugin.getInstance().getThreadManager().runTaskLater(new BlocksRegenThread(blocks, effect), TimeHandler.getTimeInTicks(secondsDelay, TimeType.SECOND));
    }

    public static void setBlock(Block block, MaterialData blockData) {
        block.getState().setData(blockData);
        block.setType(blockData.getItemType());
        block.getState().update(true);
    }

    public static void setBlock(Location loc, MaterialData data) {
        setBlock(loc.getBlock(), data);
    }

    public static void setBlock(Block block, Material changeMaterial) {
        block.setType(changeMaterial);
        block.getState().setType(changeMaterial);
        block.getState().update(true);
    }

    public static void setBlock(Location location, Material material) {
        setBlock(getBlockAt(location), material);
    }

    public static boolean isOre(Block block) {
        return isOre(block.getType());
    }

    public static boolean isOre(Material material) {
        switch (material) {
            case COAL_ORE:
            case IRON_ORE:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case REDSTONE_ORE:
            case GOLD_ORE:
            case LAPIS_ORE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSmeltableOre(Block block) {
        return Items.isSmeltableOre(block.getType());
    }

    public static boolean isOfAnyType(Block block, Material... types) {
        Set<Material> mats = Sets.newHashSet(types);
        return mats.contains(block.getType());
    }

    public static TNTPrimed spawnTNT(Location location) {
        return (TNTPrimed) location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
    }

    public static void spawnTNT(Location location, int amount) {
        for (int i = 0; i < amount; i++) {
            spawnTNT(location);
        }
    }

    public static Block getBlockFacing(Block parent, BlockFace face) {
        return parent.getRelative(face);
    }

    public static Set<Block> getBlocksSurrounding(Block parent) {
        return EnumSet.allOf(BlockFace.class).stream().map(face -> getBlockFacing(parent, face)).collect(Collectors.toSet());
    }

    public static Block getBlockAbove(Block block) {
        int[] xyz = Locations.getXYZ(block.getLocation());
        return block.getWorld().getBlockAt(xyz[0], xyz[1] - 1, xyz[2]);
    }

    public static Block getBlockBelow(Block block) {
        int[] xyz = Locations.getXYZ(block.getLocation());
        return block.getWorld().getBlockAt(xyz[0], xyz[1] + 1, xyz[2]);
    }

    public static Block getNearestEmptySpace(Block b, int maxradius) {
        BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST};
        BlockFace[][] orth = {{BlockFace.NORTH, BlockFace.EAST}, {BlockFace.UP, BlockFace.EAST}, {BlockFace.NORTH, BlockFace.UP}};
        for (int r = 0; r <= maxradius; r++) {
            for (int s = 0; s < 6; s++) {
                BlockFace f = faces[s % 3];
                BlockFace[] o = orth[s % 3];
                if (s >= 3) {
                    f = f.getOppositeFace();
                }
                Block c = b.getRelative(f, r);
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        Block a = c.getRelative(o[0], x).getRelative(o[1], y);
                        if (a.getType() == Material.AIR && a.getRelative(BlockFace.UP).getType() == Material.AIR) {
                            return a;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void setFire(Collection<Location> locations) {
        locations.forEach(l -> Blocks.setBlock(l, Material.FIRE));
    }

    public static void restoreBlocks(Map<Location, Material> materialLocations) {
        materialLocations.forEach(Blocks::setBlock);
    }


    public static FallingBlock spawnFallingBlock(Location loc, Material material) {
        return spawnFallingBlock(loc, material, 0);
    }

    public static FallingBlock spawnFallingBlock(Location loc, Material material, int dataValue) {
        return spawnFallingBlock(loc, Items.getMaterialData(material, dataValue));
    }

    public static FallingBlock spawnFallingBlock(Location loc, MaterialData data) {
        World world = loc.getWorld();
        FallingBlock block = world.spawnFallingBlock(loc, data.getItemType(), data.getData());
        return block;
    }

    private static final Set<Material> GRASS_BLACKLIST = Sets.newHashSet(
            Material.ACACIA_SAPLING,
            Material.BAMBOO_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.OAK_SAPLING,
            Material.TALL_GRASS,
            Material.DEAD_BUSH,
            Material.DANDELION,
            Material.ROSE_BUSH,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.CACTUS,
            Material.VINE
    );

    private static final Set<Material> GRASS_WHITELIST = Sets.newHashSet(
            Material.GRASS,
            Material.DIRT,
            Material.SOUL_SAND
    );

    private static final List<ChancedBlock> GRASS_PATCH_BLOCKS = Lists.newArrayList(
            ChancedBlock.of(Material.TALL_GRASS, 1, 100),
            ChancedBlock.of(Material.TALL_GRASS, 2, 45),
            ChancedBlock.of(Material.DANDELION, 0, 5),
            ChancedBlock.of(Material.POPPY, 0, 5),
            ChancedBlock.of(Material.PUMPKIN, 0, 2),
            ChancedBlock.of(Material.MELON, 0, 2),
            ChancedBlock.of(Material.BLUE_ORCHID, 1, 2),
            ChancedBlock.of(Material.ALLIUM, 2, 2),
            ChancedBlock.of(Material.AZURE_BLUET, 3, 2),
            ChancedBlock.of(Material.RED_TULIP, 4, 2),
            ChancedBlock.of(Material.ORANGE_TULIP, 5, 2),
            ChancedBlock.of(Material.WHITE_TULIP, 6, 2),
            ChancedBlock.of(Material.PINK_TULIP, 7, 2),
            ChancedBlock.of(Material.OXEYE_DAISY, 8, 2),
            ChancedBlock.of(Material.BROWN_MUSHROOM, 0, 1),
            ChancedBlock.of(Material.RED_MUSHROOM, 0, 1),
            ChancedBlock.of(Material.SUNFLOWER, 0, 5),
            ChancedBlock.of(Material.LILAC, 1, 5),
            ChancedBlock.of(Material.LEGACY_DOUBLE_PLANT, 2, 10),
            ChancedBlock.of(Material.FERN, 3, 10),
            ChancedBlock.of(Material.LARGE_FERN, 4, 5),
            ChancedBlock.of(Material.ROSE_BUSH, 5, 5),
            ChancedBlock.of(Material.PEONY, 0, 5)
    );


    public static void regrowGrass(Location loc, int radius, int density) {
        if (density > 100) {
            density = 100;
        }

        Block handle;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                handle = loc.getWorld().getHighestBlockAt((int) loc.getX() + x, (int) loc.getZ() + z);

                if (!Locations.isInRadius(loc, handle.getLocation(), radius)) {
                    continue;
                }

                Block downFace = getBlockFacing(handle, BlockFace.DOWN);

                if (!GRASS_WHITELIST.contains(downFace.getType())) {
                    continue;
                }

                Material baseMat = handle.getType();

                if (GRASS_BLACKLIST.contains(baseMat)) {
                    continue;
                }

                if (!NumberUtil.percentCheck(density)) {
                    continue;
                }

                ChancedBlock replacementBlock = null;

                while (true) {
                    replacementBlock = ListUtils.getRandom(GRASS_PATCH_BLOCKS);

                    if (replacementBlock.pass()) {
                        break;
                    }
                }

                handle.setType(replacementBlock.getMaterial());
            }
        }
    }

    private static double getDistance(Location pos1, Location pos2) {
        double ySize = pos1.getY() - pos2.getY();
        double zSize = pos1.getZ() - pos2.getZ();
        double xSize = pos1.getX() - pos2.getX();

        double distance = Math.sqrt((xSize * xSize) + (ySize * ySize) + (zSize + zSize));
        return distance;
    }
}
