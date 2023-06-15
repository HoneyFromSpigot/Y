package io.github.thewebcode.yplugin.entity;

import com.google.common.collect.Sets;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.inventory.ArmorInventory;
import io.github.thewebcode.yplugin.inventory.ArmorSlot;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.location.Locations;
import io.github.thewebcode.yplugin.potion.Potions;
import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import io.github.thewebcode.yplugin.warp.Warp;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class Entities {

    public static Set<LivingEntity> spawnLivingEntity(EntityType entityType, Location location, int amount) {
        Set<LivingEntity> entities = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            entities.add(spawnLivingEntity(entityType, location));
        }
        return entities;
    }


    public static LivingEntity spawnLivingEntity(EntityType entityType, Location location) {
        return (LivingEntity) location.getWorld().spawnEntity(location, entityType);
    }

    public static TNTPrimed spawnPrimedTnt(Location location) {
        return location.getWorld().spawn(location, TNTPrimed.class);
    }

    public static Firework spawnFireworks(Location loc) {
        return (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
    }

    public static Zombie spawnZombie(Location location, boolean isBaby, boolean isVillager) {
        Zombie zombie = (Zombie) spawnLivingEntity(EntityType.ZOMBIE, location);
        zombie.setBaby(isBaby);
        zombie.setVillager(isVillager);
        return zombie;
    }

    public static PigZombie spawnPigZombie(Location location, boolean isBaby) {
        PigZombie pigZombie = (PigZombie) spawnLivingEntity(EntityType.ZOMBIFIED_PIGLIN, location);
        pigZombie.setBaby(isBaby);
        return pigZombie;
    }

    public static Zombie spawnBabyZombie(Location location, boolean isVillager) {
        return spawnZombie(location, true, isVillager);
    }

    public static Sheep spawnRandomSheep(Location location) {
        Sheep sheep = (Sheep) spawnLivingEntity(EntityType.SHEEP, location);
        sheep.setColor(Items.getRandomDyeColor());
        return sheep;
    }

    public static Bat spawnInvisibleBat(Location loc) {
        World world = loc.getWorld();
        Bat bat = world.spawn(loc, Bat.class);
        addPotionEffect(bat, Potions.getPotionEffect(PotionEffectType.INVISIBILITY, 1, Integer.MAX_VALUE));
        bat.setNoDamageTicks(Integer.MAX_VALUE);
        bat.setRemoveWhenFarAway(false);
        bat.setVelocity(new Vector(0, 0, 0));
        return bat;
    }

    public static ChatColor getHealthBarColor(double enemyHealthPercentage) {
        ChatColor healthBarColor = ChatColor.GREEN;
        if (enemyHealthPercentage >= 35 && enemyHealthPercentage <= 65) {
            healthBarColor = ChatColor.YELLOW;
        } else if (enemyHealthPercentage < 35) {
            healthBarColor = ChatColor.RED;
        }
        return healthBarColor;
    }

    public static boolean hasKiller(LivingEntity entity) {
        return entity.getKiller() != null;
    }

    public static ChatColor getHealthBarColor(Damageable entity) {
        return getHealthBarColor((entity.getHealth() / entity.getMaxHealth()) * 100);
    }

    public static void setName(LivingEntity entity, String name) {
        entity.setCustomName(name);
    }

    public static void setName(LivingEntity entity, String name, boolean isVisible) {
        setName(entity, name);
        entity.setCustomNameVisible(isVisible);
    }

    public static String getName(Entity entity) {
        if (!hasName(entity)) {
            return getDefaultName(entity.getType());
        } else {
            return entity.getCustomName();
        }
    }

    public static boolean hasName(Entity entity) {
        return entity.getCustomName() != null;
    }

    public static String getDefaultName(LivingEntity entity) {
        return getDefaultName(entity.getType());
    }

    public static String getDefaultName(EntityType type) {
        return WordUtils.capitalizeFully(type.name().toLowerCase().replace("_", " "));
    }

    public static double getCurrentHealth(Damageable entity) {
        return entity.getHealth();
    }

    public static void addPotionEffect(LivingEntity entity, PotionEffect... effects) {
        for (PotionEffect effect : effects) {
            entity.addPotionEffect(effect);
        }
    }

    public static void addPotionEffects(LivingEntity entity, Collection<PotionEffect> effects) {
        entity.addPotionEffects(effects);
    }

    public static void setHealth(LivingEntity entity, double health) {
        double maxHealth = getMaxHealth(entity);
        entity.setHealth(health <= maxHealth ? health : maxHealth);
    }

    public static double getMaxHealth(Damageable entity) {
        return entity.getMaxHealth();
    }

    public static void setMaxHealth(LivingEntity entity, double health) {
        entity.setMaxHealth(health);
    }

    public static void setEquipment(LivingEntity entity, ArmorSlot slot, ItemStack item) {
        EntityEquipment inv = entity.getEquipment();
        switch (slot) {
            case BOOTS:
                inv.setBoots(item);
                break;
            case CHEST:
                inv.setChestplate(item);
                break;
            case HELMET:
                inv.setHelmet(item);
                break;
            case LEGGINGS:
                inv.setLeggings(item);
                break;
            case MAIN_HAND:
                inv.setItemInMainHand(item);
                break;
            case OFF_HAND:
                inv.setItemInOffHand(item);
                break;
            default:
                break;
        }
    }

    public static void setEquipment(LivingEntity entity, ArmorInventory inventory) {
        inventory.getArmor().forEach((key, value) -> Entities.setEquipment(entity, key, value));
    }

    public static void setEquipment(Collection<? extends LivingEntity> entities, ArmorInventory inventory) {
        entities.forEach(inventory::equip);
    }

    public static EntityType getTypeByName(String type) {
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type);
            if (entityType != null) {
                return entityType;
            }
        } catch (Exception e) {

        }

        String entityInput = type.toLowerCase().replace("_", "");
        entityType = MobType.getTypeByName(entityInput);
        if (entityType != null) {
            return entityType;
        } else {
            return EntityType.UNKNOWN;
        }
    }

    public static void knockbackEntity(LivingEntity entity) {
        knockbackEntity(entity, -1);
    }

    public static void knockbackEntity(LivingEntity entity, int force) {
        entity.setVelocity(entity.getLocation().getDirection().multiply(force));
    }

    public static void launchUpwards(LivingEntity entity, int amount, double force) {
        Vector entityVector = entity.getVelocity();
        entityVector.add(new Vector(0, amount, 0)).multiply(force);
        entity.setVelocity(entityVector);
    }

    public static void launchSnowball(LivingEntity entity) {
        launchSnowball(entity, 1, 2);
    }

    public static void launchSnowball(LivingEntity entity, int force) {
        launchSnowball(entity, 1, force);
    }

    public static void launchSnowball(LivingEntity entity, int amount, int force) {
        for (int i = 0; i < amount; i++) {
            Snowball snowball = entity.launchProjectile(Snowball.class);
            snowball.setVelocity(snowball.getVelocity().multiply(force));
        }
    }

    public static void remove(Entity... entities) {
        for (Entity e : entities) {
            if (e instanceof Player) {
                continue;
            }
            e.remove();
        }
    }

    public static void removeEntitySafely(Entity... entities) {
        YPlugin.getInstance().getThreadManager().runTaskOneTickLater(() -> {
            for (Entity e : entities) {
                if (e instanceof Player) {
                    continue;
                }
                e.remove();
            }
        });
    }

    public static LivingEntity getEntityByUUID(World world, UUID entityUUID) {
        for (LivingEntity entity : world.getLivingEntities()) {
            if (entity.getUniqueId().equals(entityUUID)) {
                return entity;
            }
        }
        return null;
    }

    public static LivingEntity getEntityByUUID(UUID id) {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity.getUniqueId().equals(id)) {
                    return entity;
                }
            }
        }

        return null;
    }

    public static Set<LivingEntity> getLivingEntitiesNear(Entity entity, double x, double y, double z) {
        Set<LivingEntity> entities = new HashSet<>();
        entity.getNearbyEntities(x, y, z).stream().filter(e -> e instanceof LivingEntity).forEach(e -> entities.add((LivingEntity) e));
        return entities;
    }

    public static Set<LivingEntity> getLivingEntitiesNear(Entity entity, double radius) {
        return getLivingEntitiesNear(entity, radius, radius, radius);
    }

    public static Set<Entity> selectEntitiesNear(Entity entity, double x, double y, double z, EntityType... types) {
        Set<Entity> entities = new HashSet<>();

        List<Entity> nearby = entity.getNearbyEntities(x, y, z);
        if (types == null || types.length == 0) {
            entities = Sets.newHashSet(nearby);
            return entities;
        }

        Set<EntityType> typeSet = Sets.newHashSet(types);
        entities = nearby.stream().filter(e -> typeSet.contains(e.getType())).collect(Collectors.toSet());
        return entities;
    }

    public static Set<LivingEntity> getLivingEntitiesNearLocation(Location center, double radius) {
        Set<LivingEntity> entities = new HashSet<>();
        for (LivingEntity entity : center.getWorld().getLivingEntities()) {
            if (Locations.isEntityInRadius(center, radius, entity)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static Set<Entity> getEntitiesNearLocation(Location center, int radius) {
        Set<Entity> entities = new HashSet<>();
        for (Entity entity : center.getWorld().getEntities()) {
            if (Locations.isEntityInRadius(center, radius, entity)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static Set<Entity> selectEntitiesNearLocation(Location location, int radius, EntityType... types) {
        Set<EntityType> validEntityTypes = Sets.newHashSet(types);
        return getEntitiesNearLocation(location, radius).stream().filter(e -> validEntityTypes.contains(e.getType())).collect(Collectors.toSet());
    }

    public static Set<LivingEntity> selectLivingEntitiesNearLocation(Location location, int radius, EntityType... types) {
        Set<EntityType> validEntityTypes = Sets.newHashSet(types);
        return getLivingEntitiesNearLocation(location, radius).stream().filter(e -> validEntityTypes.contains(e.getType())).collect(Collectors.toSet());
    }

    public static Set<Item> getDroppedItemsNearLocation(Location center, int radius) {
        Set<Item> items = new HashSet<>();
        for (Item item : center.getWorld().getEntitiesByClass(Item.class)) {
            if (Locations.isEntityInRadius(center, radius, item)) {
                items.add(item);
            }
        }
        return items;
    }

    public static Set<LivingEntity> filterCollection(Collection<LivingEntity> entities, EntityType... types) {
        Set<EntityType> validTypes = Sets.newHashSet(types);
        return entities.stream().filter(e -> validTypes.contains(e.getType())).collect(Collectors.toSet());
    }

    public static Set<LivingEntity> reduceCollection(Collection<LivingEntity> entities, EntityType... types) {
        Set<EntityType> invalidTypes = Sets.newHashSet(types);
        return entities.stream().filter(e -> !invalidTypes.contains(e.getType())).collect(Collectors.toSet());
    }

    public static void damage(Damageable target, double damage) {
        if (target instanceof Player) {
            if (YPlugin.getInstance().getPlayerHandler().getData((Player) target).hasGodMode()) {
                return;
            }
        }
        target.damage(damage);
    }

    public static void damage(Damageable target, double damage, LivingEntity damager) {
        if (target instanceof Player) {
            if (YPlugin.getInstance().getPlayerHandler().getData((Player) target).hasGodMode()) {
                return;
            }
        }

        target.damage(damage, damager);
    }

    public static void burn(Entity target, int fireTicks) {
        target.setFireTicks(fireTicks);
    }

    public static void burn(Entity target, int amount, TimeType timeType) {
        target.setFireTicks((int) TimeHandler.getTimeInTicks(amount, timeType));
    }

    public static void kill(Damageable... targets) {
        for (Damageable entity : targets) {
            kill(entity);
        }
    }

    public static void kill(Damageable target) {
        EntityDamageEvent event = new EntityDamageEvent(target, EntityDamageEvent.DamageCause.CUSTOM, Double.MAX_VALUE);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        target.setHealth(0.0);
    }

    public static void removePotionEffects(LivingEntity entity) {
        for (PotionEffect effect : entity.getActivePotionEffects()) {
            entity.removePotionEffect(effect.getType());
        }
    }

    public static void removeFire(Entity entity) {
        entity.setFireTicks(0);
    }

    public static void restoreHealth(LivingEntity livingEntity) {
        setHealth(livingEntity, getMaxHealth(livingEntity));
    }

    public static void teleport(Entity entity, Location targetLocation) {
        entity.teleport(targetLocation);
    }

    public static void teleport(Entity entity, Warp warp) {
        teleport(entity, warp.getLocation());
    }

    public static void pullEntityToLocation(final Entity e, Location loc) {
        Location entityLoc = e.getLocation();

        entityLoc.setY(entityLoc.getY() + 0.5);
        e.teleport(entityLoc);

        double g = -0.08;
        double d = loc.distance(entityLoc);
        double v_x = (1.0 + 0.07 * d) * (loc.getX() - entityLoc.getX()) / d;
        double v_y = (1.0 + 0.03 * d) * (loc.getY() - entityLoc.getY()) / d - 0.5 * g * d;
        double v_z = (1.0 + 0.07 * d) * (loc.getZ() - entityLoc.getZ()) / d;

        Vector v = e.getVelocity();
        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);
        e.setVelocity(v);
    }

    public static boolean hasPassenger(Entity entity) {
        return entity.getPassenger() != null;
    }

    public static void adjustHealth(Damageable subject, double ratio) {
        double maxHealth = getMaxHealth(subject);
        double currentHealth = getCurrentHealth(subject);

        double adjustedMax = maxHealth * ratio;
        double adjustedHealth = currentHealth * ratio;

        subject.setMaxHealth(adjustedMax);
        subject.setHealth(adjustedHealth);
    }

    public static boolean onBlock(Entity entity) {
        Location loc = entity.getLocation();

        double locX = loc.getX();
        double locZ = loc.getZ();
        double locY = loc.getY();

        double xMod = locX % 1.0D;
        if (locX < 0.0D) {
            xMod += 1.0D;
        }

        double zMod = locZ % 1.0D;
        if (locZ < 0.0D) {
            zMod += 1.0D;
        }

        int xMin = 0;
        int xMax = 0;
        int zMin = 0;
        int zMax = 0;

        if (xMod < 0.3D) {
            xMin = -1;
        }
        if (xMod > 0.7D) {
            xMax = 1;
        }
        if (zMod < 0.3D) {
            zMin = -1;
        }
        if (zMod > 0.7D) {
            zMax = 1;
        }

        for (int x = xMin; x <= xMax; x++) {
            for (int z = zMin; z <= zMax; z++) {

                Block locBlock = loc.add(x, -0.5D, z).getBlock();

                Material locBlockType = locBlock.getType();
                if (locBlockType != Material.AIR && !locBlock.isLiquid()) {
                    return true;
                }

                Material materialBeneathPlayer = loc.add(x, -1.5D, z).getBlock().getType();
                if (locY % 0.5D != 0.0D) {
                    continue;
                }
                switch (materialBeneathPlayer) {
                    case ACACIA_FENCE:
                    case BIRCH_FENCE:
                    case DARK_OAK_FENCE:
                    case JUNGLE_FENCE:
                    case NETHER_BRICK_FENCE:
                    case OAK_FENCE:
                    case SPRUCE_FENCE:
                    case ANDESITE_WALL:
                    case BRICK_WALL:
                    case COBBLESTONE_WALL:
                    case DIORITE_WALL:
                    case GRANITE_WALL:
                    case END_STONE_BRICK_WALL:
                    case MOSSY_COBBLESTONE_WALL:
                    case MOSSY_STONE_BRICK_WALL:
                    case RED_NETHER_BRICK_WALL:
                    case NETHER_BRICK_WALL:
                    case PRISMARINE_WALL:
                    case RED_SANDSTONE_WALL:
                    case SANDSTONE_WALL:
                    case STONE_BRICK_WALL:
                        return true;
                    default:
                        break;
                }
            }
        }
        return false;
    }

    public static boolean isHostile(LivingEntity entity) {
        return MobType.isHostile(entity.getType());
    }

    public static boolean isMob(Entity entity) {
        return MobType.isMob(entity.getType());
    }

    public static void assignData(Entity entity, MobData data) {
        if (entity instanceof Ageable) {
            Ageable ageable = (Ageable) entity;
            if (data.isBaby()) {
                ageable.setBaby();
            } else {
                if (data.getAgeMin() > 0 && data.getAgeMax() > data.getAgeMin()) {
                    ageable.setAge(NumberUtil.getRandomInRange(data.getAgeMin(), data.getAgeMax()));
                } else if (data.getAge() > 0) {
                    ageable.setAge(data.getAge());
                } else {
                    ageable.setAdult();
                }
            }
        }

        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            zombie.setBaby(data.isBaby());
            zombie.setVillager(zombie.isVillager());
        }

        if (entity instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) entity;
            skeleton.setSkeletonType(data.getSkeletonType());
        }

        if (entity instanceof Slime) {
            Slime slime = (Slime) entity;
            if (data.getSizeMin() > 0 && data.getSizeMax() > data.getSizeMin()) {
                slime.setSize(NumberUtil.getRandomInRange(data.getSizeMin(), data.getSizeMax()));
            } else if (data.getSize() > 0) {
                slime.setSize(data.getSize());
            }
        }

        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            creeper.setPowered(data.isPowered());
        }

        if (entity instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) entity;
            if (data.getHealth() > 0 && data.getMaxHealth() >= data.getHealth()) {

                Entities.setMaxHealth(le, data.getMaxHealth());
                Entities.setHealth(le, data.getHealth());
            }

            if (!StringUtils.isEmpty(data.getName())) {
                Entities.setName(le, data.getName(), true);
            }
            Entities.setEquipment(le, data.getArmorInventory());
        }
    }
}