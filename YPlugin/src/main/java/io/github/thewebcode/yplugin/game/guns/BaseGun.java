package io.github.thewebcode.yplugin.game.guns;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.entity.Entities;
import io.github.thewebcode.yplugin.exceptions.ProjectileCreationException;
import io.github.thewebcode.yplugin.game.gadget.ItemGadget;
import io.github.thewebcode.yplugin.inventory.Inventories;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;

import io.github.thewebcode.yplugin.yml.Path;
import io.github.thewebcode.yplugin.yml.Skip;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public abstract class BaseGun extends ItemGadget implements Gun {
    private static final YPlugin commons = YPlugin.getInstance();
    private static final Random random = new Random();

    @Path("item")
    private ItemStack gun;

    @Skip
    private String gunBaseName;

    @Path("properties")
    private GunProperties properties = new GunProperties();

    @Path("bullet-properties")
    private BulletProperties bullets = new BulletProperties();

    @Skip
    private Map<UUID, Integer> ammoCounts = new HashMap<>();

    private Map<UUID, Long> shootDelays = new HashMap<>();

    private BulletBuilder builder = null;

    private BulletActions actions;

    public BaseGun(ItemStack gun, GunProperties properties, BulletProperties bulletProperties) {
        super(gun);
        this.gun = gun;
        this.gunBaseName = Items.getName(gun);
        this.properties = properties;
        this.bullets = bulletProperties;
    }

    public BaseGun(ItemStack item) {
        super(item);
        gun = item.clone();
        this.gunBaseName = Items.getName(gun);
    }

    public BaseGun(ItemBuilder builder) {
        super(builder);
        gun = getItem();
        this.gunBaseName = Items.getName(gun);
    }

    private void initBuilder() {
        if (builder == null) {
            builder = new BulletBuilder(properties.ammunition()).gun(this);
        }

        builder.damage(damage()).power(bullets.speed);
    }

    @Override
    public void perform(Player holder) {
        initBuilder();
        if (isOnCooldown(holder)) {
            return;
        }
        if (needsReload(holder)) {
            if (!reload(holder)) {
                return;
            }
        }

        int roundsToShoot = getRoundsToShoot(holder);

        boolean scheduleReload = false;
        if (roundsToShoot < properties.roundsPerShot) {
            scheduleReload = true;
        }
        if (!scheduleReload) {

            int ammoAfterShooting = getAmmo(holder);
            if (properties.clusterShot) {
                ammoAfterShooting -= 1;
            } else {
                ammoAfterShooting -= roundsToShoot;
            }

            setAmmo(holder, ammoAfterShooting);
        }
        builder = builder.shooter(holder);

        if (properties.clusterShot) {
            for (int i = 0; i < roundsToShoot; i++) {
                try {
                    builder.shoot();
                } catch (ProjectileCreationException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < roundsToShoot; i++) {
                YPlugin.getInstance().getThreadManager().runTaskLater(() -> {
                    try {
                        builder.shoot();
                    } catch (ProjectileCreationException e) {
                        e.printStackTrace();
                    }
                }, i * bullets.delay);
            }
        }
        addCooldown(holder);
        onFire(holder);

        if (scheduleReload || getAmmo(holder) == 0) {
            reload(holder);
        }
    }

    @Override
    public void onDrop(Player p, Item item) {
        reload(p);
    }

    public boolean reload(final Player player) {

        UUID id = player.getUniqueId();

        final MinecraftPlayer mcPlayer = commons.getPlayerHandler().getData(id);
        if (mcPlayer.isReloading()) {
            return false;
        }

        mcPlayer.setReloading(properties.reloadSpeed);
        commons.getThreadManager().runTaskLater(() -> {
            if (!Players.isOnline(id)) {
                return;
            }

            if (ammoCounts.containsKey(id)) {
                if (properties.reloadMessage) {
                    Chat.message(player, Messages.gadgetReloaded(this));
                }
            }
            if (properties.takeAmmunition) {
            }

            setAmmo(player, properties.clipSize);
            mcPlayer.setReloading(0);
        }, properties.reloadSpeed);
        return true;
    }

    private void addCooldown(Player player) {
        shootDelays.put(player.getUniqueId(), System.currentTimeMillis() + properties.shotDelay);
    }

    private boolean isOnCooldown(Player player) {
        UUID id = player.getUniqueId();
        if (!shootDelays.containsKey(id)) {
            return false;
        }

        return shootDelays.get(id) >= System.currentTimeMillis();
    }

    public boolean needsReload(Player player) {
        UUID id = player.getUniqueId();

        if (!ammoCounts.containsKey(id)) {
            return false;
        }

        int ammoCount = ammoCounts.get(id);

        return ammoCount <= 0;
    }

    public void damage(LivingEntity damaged, Player shooter) {
        Entities.damage(damaged, damage(), shooter);
        actions.onHit(shooter, damaged);
    }

    @Override
    public BulletActions getBulletActions() {
        return actions;
    }

    @Override
    public void setBulletActions(BulletActions actions) {
        this.actions = actions;
    }

    @Override
    public GunProperties properties() {
        return properties;
    }

    public void properties(GunProperties properties) {
        this.properties = properties;
    }

    public int getAmmo(Player player) {
        UUID id = player.getUniqueId();
        if (!ammoCounts.containsKey(id)) {
            ammoCounts.put(id, properties.clipSize);
        }
        return ammoCounts.get(player.getUniqueId());
    }

    public void setAmmo(Player player, int amt) {
        ammoCounts.put(player.getUniqueId(), amt);

        if (properties.displayAmmo) {
            giveGunAmmoCount(player);
        }
    }

    private void giveGunAmmoCount(Player player) {
        int slot = Inventories.getSlotOf(player.getInventory(), gun.getType(), Items.getName(gun));

        if (slot == -1) {
            commons.debug("Unable to get slot of " + getItemName() + " on player " + player.getName());
            return;
        }

        ItemStack item = Players.getItem(player, slot);
        Items.setName(item, Messages.gunNameAmmoFormat(getItemName(), getAmmo(player)));
    }

    @Override
    public abstract void onFire(Player shooter);

    @Override
    public double damage() {
        return properties.getDamage() + bullets.damage + (bullets.damage * random.nextDouble()) - (bullets.damage * random.nextDouble());
    }

    @Override
    public BulletProperties bulletProperties() {
        return bullets;
    }

    private int getRoundsToShoot(Player player) {
        int ammoCount = getAmmo(player);

        int shots = properties.roundsPerShot;

        if (ammoCount < shots) {
            return shots - ammoCount;
        }

        return shots;
    }

    public String getItemName() {
        return gunBaseName;
    }

    public BulletBuilder getBulletBuilder() {
        initBuilder();
        return builder;
    }
}
