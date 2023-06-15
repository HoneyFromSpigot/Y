package io.github.thewebcode.yplugin.game.guns;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.entity.Entities;
import io.github.thewebcode.yplugin.game.clause.BulletDamageEntityClause;
import io.github.thewebcode.yplugin.game.event.BulletHitBlockEvent;
import io.github.thewebcode.yplugin.game.event.BulletHitCreatureEvent;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.plugin.Plugins;
import io.github.thewebcode.yplugin.vector.Vectors;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

import java.util.*;

public abstract class BaseBullet implements Metadatable {

    public static final double BULLET_SCAN_RADIUS = 1.5;
    private static final YPlugin commons = YPlugin.getInstance();

    private static final Random random = new Random();

    private static final int PICKUP_DELAY = 999999;

    private Map<String, List<MetadataValue>> metadata = new HashMap<>();
    private UUID shooter;

    private double damage;
    private double force;
    private double spread;

    private Gun gun;

    private boolean gunless = false;

    private ItemStack itemStack;

    private Item item;

    private BulletDamageEntityClause damageConditions = null;

    public BaseBullet(UUID shooter, Gun gun, ItemStack item, double force, double damage, double spread, BulletDamageEntityClause damageConditions) {
        this(Players.getPlayer(shooter), gun, item, force, damage, spread);
        this.damageConditions = damageConditions;
    }

    public BaseBullet(UUID shooter, Gun gun, ItemStack item, double force, double damage, double spread) {
        this(Players.getPlayer(shooter), gun, item, force, damage, spread);
    }

    public BaseBullet(Player shooter, Gun gun, ItemStack item, double force, double damage, double spread) {
        this.shooter = shooter.getUniqueId();
        this.gun = gun;
        this.force = force;
        this.itemStack = item;
        this.damage = damage;
        this.spread = spread;
    }

    @Override
    public void setMetadata(String key, MetadataValue value) {
        if (!hasMetadata(key)) {
            metadata.put(key, Arrays.asList(value));
            return;
        }

        metadata.get(key).add(value);
    }

    @Override
    public List<MetadataValue> getMetadata(String key) {
        return metadata.get(key);
    }

    @Override
    public boolean hasMetadata(String key) {
        return metadata.containsKey(key);
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) {
        metadata.remove(s);
    }

    public void fire() {
        Player p = getShooter();

        Location eyeLoc = p.getEyeLocation();
        double px = eyeLoc.getX();
        double py = eyeLoc.getY();
        double pz = eyeLoc.getZ();
        double yaw = Math.toRadians(eyeLoc.getYaw() + 90.0f);
        double pitch = Math.toRadians(eyeLoc.getPitch() + 90.0f);
        double[] appliableSpread = {
                (random.nextDouble() - random.nextDouble()) * spread * 0.1,
                (random.nextDouble() - random.nextDouble()) * spread * 0.1,
                (random.nextDouble() - random.nextDouble()) * spread * 0.1
        };
        double x = Math.sin(pitch) * Math.cos(yaw) + appliableSpread[0];
        double y = Math.sin(pitch) * Math.sin(yaw) + appliableSpread[1];
        double z = Math.cos(pitch) + appliableSpread[2];

        World pw = p.getWorld();

        Item bullet = Worlds.dropItem(p.getLocation(), itemStack);
        bullet.setPickupDelay(PICKUP_DELAY);
        bullet.setVelocity(Vectors.direction(p.getEyeLocation(), Players.getTargetLocation(p)).multiply(force * 2));

        item = bullet;

        int range = getGun().properties().range;

        for (int i = 0; i < range; i++) {
            double modX = px + i * x;
            double modY = py + i * z;
            double modZ = pz + i * y;

            Location l = new Location(pw, modX, modY, modZ);
            onTravel(l);

            Block locBlock = l.getBlock();
            if (locBlock.getType().isSolid()) {
                BulletHitBlockEvent event = new BulletHitBlockEvent(this, locBlock);
                Plugins.callEvent(event);
                BulletHitBlockEvent.handle(event);
                removeBullet();
                break;
            }
            if (item.isOnGround()) {
                Chat.debug("Bullet is on the ground!");
                removeBullet();
                return;
            }

            Set<LivingEntity> entities = Entities.getLivingEntitiesNearLocation(l, 2);
            for (LivingEntity entity : entities) {
                if (entity.getUniqueId().equals(shooter)) {
                    continue;
                }

                if (damageConditions != null && !damageConditions.damage(p, entity)) {
                    continue;
                }
                BulletHitCreatureEvent event = new BulletHitCreatureEvent(this, entity);
                Plugins.callEvent(event);
                BulletHitCreatureEvent.handle(event);
                removeBullet();
                return;
            }
        }
    }

    private void removeBullet() {
        YPlugin.getInstance().getThreadManager().runTaskLater(() -> {
            item.remove();
        }, 40 - (long) (getGun().bulletProperties().speed * 2));
    }

    public double getDamage() {
        return damage;
    }

    public Player getShooter() {
        return Players.getPlayer(shooter);
    }

    public Gun getGun() {
        return gun;
    }

    protected ItemStack getItemStack() {
        return itemStack;
    }

    protected Item getItem() {
        return item;
    }

    protected static Random getRandom() {
        return random;
    }

    public double getSpread() {
        return spread;
    }

    protected void setItem(Item i) {
        this.item = i;
    }

    public abstract void onTravel(Location l);


}
