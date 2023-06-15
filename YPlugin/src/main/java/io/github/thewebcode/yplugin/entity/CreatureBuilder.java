package io.github.thewebcode.yplugin.entity;

import io.github.thewebcode.yplugin.inventory.ArmorBuilder;
import io.github.thewebcode.yplugin.inventory.ArmorInventory;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.*;

import java.util.HashSet;
import java.util.Set;

public class CreatureBuilder {
    private EntityType type;

    private double health = 0;

    private double maxHealth = 0;

    private boolean baby = false;

    private boolean villager = false;

    private boolean powered = false;

    private String name = "";

    private int age = 0;

    private int ageMin = 0;

    private int ageMax = 0;

    private int size = 0;

    private int sizeMin = 0;

    private int sizeMax = 0;

    private ArmorBuilder armorBuilder = new ArmorBuilder();

    public static CreatureBuilder clone(Entity entity) {

        CreatureBuilder builder = new CreatureBuilder(entity.getType());

        if (entity instanceof Ageable) {
            Ageable agedEntity = (Ageable) entity;

            builder.age(agedEntity.getAge());
        }

        if (entity instanceof Villager) {
            Villager villager = (Villager) entity;
            builder.asVillager(true);
        }

        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;

            builder.asVillager(zombie.isVillager());
        }

        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;

            builder.powered(creeper.isPowered());
        }

        if (Entities.hasName(entity)) {
            builder.name(entity.getCustomName());
        }

        if (entity instanceof LivingEntity) {
            LivingEntity creature = (LivingEntity) entity;
            builder.maxHealth(Entities.getMaxHealth(creature))
                    .health(Entities.getCurrentHealth(creature))
                    .armor(new ArmorInventory(creature.getEquipment().getArmorContents()));


        }

        if (entity instanceof Slime) {
            Slime slime = (Slime) entity;
            builder.size(slime.getSize());
        }

        return builder;
    }

    public CreatureBuilder(EntityType type) {
        this.type = type;
    }

    public CreatureBuilder(MobType type) {
        this.type = type.getEntityType();

    }

    public CreatureBuilder name(String name) {
        this.name = StringUtil.colorize(name);
        return this;
    }

    public CreatureBuilder health(double health) {
        this.health = health;
        return this;
    }

    public CreatureBuilder maxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }

    public CreatureBuilder age(int min, int max) {
        this.ageMin = min;
        this.ageMax = max;
        return this;
    }

    public CreatureBuilder age(int age) {
        this.age = age;
        return this;
    }

    public CreatureBuilder size(int min, int max) {
        this.sizeMin = min;
        this.sizeMax = max;
        return this;
    }

    public CreatureBuilder size(int size) {
        this.size = size;
        return this;
    }

    public CreatureBuilder powered() {
        this.powered = true;
        return this;
    }

    public CreatureBuilder powered(boolean value) {
        this.powered = value;
        return this;
    }

    public CreatureBuilder asBaby(boolean baby) {
        this.baby = baby;
        return this;
    }

    public CreatureBuilder asVillager(boolean villager) {
        this.villager = villager;
        return this;
    }

    public ArmorBuilder armor() {
        return armorBuilder.parent(this);
    }

    public CreatureBuilder armor(ArmorInventory armor) {
        armor().withHelmet(armor.getHelmet()).withBoots(armor.getBoots()).withChest(armor.getChest()).withLeggings(armor.getLegs()).withMainHand(armor.getMainHand()).withOffHand(armor.getOffHand());
        return this;
    }

    public LivingEntity spawn(Location location) {
        LivingEntity entity = Entities.spawnLivingEntity(type, location);

        if (entity instanceof Ageable) {
            Ageable ageable = (Ageable) entity;
            if (baby) {
                ageable.setBaby();
            } else {
                if (ageMin > 0 && ageMax > ageMin) {
                    ageable.setAge(NumberUtil.getRandomInRange(ageMin, ageMax));
                } else if (age > 0) {
                    ageable.setAge(age);
                } else {
                    ageable.setAdult();
                }
            }
        }

        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            zombie.setBaby(baby);
        }

        if (entity instanceof Slime) {
            Slime slime = (Slime) entity;
            if (sizeMin > 0 && sizeMax > sizeMin) {
                slime.setSize(NumberUtil.getRandomInRange(sizeMin, sizeMax));
            } else if (size > 0) {
                slime.setSize(size);
            }
        }

        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            creeper.setPowered(powered);
        }

        if (health > 0 && maxHealth >= health) {
            Entities.setMaxHealth(entity, maxHealth);
            Entities.setHealth(entity, health);
        }

        if (!StringUtils.isEmpty(name)) {
            Entities.setName(entity, name, true);
        }
        Entities.setEquipment(entity, armorBuilder.toInventory());
        return entity;
    }

    public MobData toSpawnData() {
        MobData data = new MobData();

        data.setEntityType(type);
        data.setAge(age);
        data.setAgeMax(ageMax);
        data.setAgeMin(ageMin);
        data.setArmorInventory(armorBuilder.toInventory());
        data.setHealth(health);
        data.setMaxHealth(maxHealth);
        data.setBaby(baby);
        data.setVillager(villager);
        data.setName(name);
        data.setPowered(powered);
        data.setSize(size);
        data.setSizeMin(sizeMin);
        data.setSizeMax(sizeMax);

        return data;
    }

    public Set<LivingEntity> spawn(Location location, int amount) {
        Set<LivingEntity> entities = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            entities.add(spawn(location));
        }
        return entities;
    }

    protected EntityType getType() {
        return type;
    }

    protected double getHealth() {
        return health;
    }

    protected double getMaxHealth() {
        return maxHealth;
    }

    protected boolean isBaby() {
        return baby;
    }

    protected boolean isVillager() {
        return villager;
    }

    protected boolean isPowered() {
        return powered;
    }

    protected String getName() {
        return name;
    }

    protected int getAge() {
        return age;
    }

    protected int getAgeMin() {
        return ageMin;
    }

    protected int getAgeMax() {
        return ageMax;
    }

    protected int getSize() {
        return size;
    }

    protected int getSizeMin() {
        return sizeMin;
    }

    protected int getSizeMax() {
        return sizeMax;
    }

    protected ArmorBuilder getArmorBuilder() {
        return armorBuilder;
    }

    public static CreatureBuilder of(EntityType type) {
        return new CreatureBuilder(type);
    }
}
