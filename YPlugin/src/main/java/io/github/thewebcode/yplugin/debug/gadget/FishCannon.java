package io.github.thewebcode.yplugin.debug.gadget;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.effect.Effects;
import io.github.thewebcode.yplugin.effect.ParticleEffect;
import io.github.thewebcode.yplugin.effect.Particles;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.game.guns.BaseGun;
import io.github.thewebcode.yplugin.game.guns.BulletActions;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.location.Locations;
import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FishCannon extends BaseGun {
    private static FishCannon instance = null;

    public static FishCannon getInstance() {
        if (instance == null) {
            instance = new FishCannon();
            Gadgets.registerGadget(instance);
        }
        return instance;
    }

    public FishCannon() {
        super(ItemBuilder.of(Material.DIAMOND_HORSE_ARMOR).name("&eFish Cannon"));
        setBulletActions(FishCannonAction.getInstance());
        initProperties();
    }

    private void initProperties() {
        properties().ammunition(ItemBuilder.of(Material.PUFFERFISH).name("&3Live Ammo")).roundsPerShot(2).shotDelay(5).clipSize(100).reloadSpeed(2);
        bulletProperties().damage(10).delayBetweenRounds(1).spread(1.2).speed(5).effect(ParticleEffect.WATER_BUBBLE);
    }

    @Override
    public void onFire(Player shooter) {
    }

    private static class FishCannonAction implements BulletActions {
        private static FishCannonAction instance;

        public static FishCannonAction getInstance() {
            if (instance == null) {
                instance = new FishCannonAction();
            }
            return instance;
        }

        @Override
        public void onHit(Player player, LivingEntity damaged) {
            Location hitLoc = damaged.getLocation();

            Effects.explode(hitLoc, 1.0f, false, false);

            ItemStack item = Items.makeItem(Material.COOKED_SALMON);

            List<Location> circle = Locations.getCircle(hitLoc, 3);
            circle.forEach(l -> {
                Item dropItem = Worlds.dropItem(l, item);
                dropItem.setFireTicks((int) TimeHandler.getTimeInTicks(4, TimeType.SECOND));
            });

            Worlds.clearDroppedItems(hitLoc, 3, 3, TimeType.SECOND);
        }

        @Override
        public void onHit(Player player, Block block) {
            Location loc = block.getLocation();
            Chat.actionMessage(player, String.format("&aIt's a hit &e(%s,%s,%s)!", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        }
    }
}
