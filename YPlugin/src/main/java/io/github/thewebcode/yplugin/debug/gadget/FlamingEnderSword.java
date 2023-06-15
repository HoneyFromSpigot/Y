package io.github.thewebcode.yplugin.debug.gadget;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.effect.ParticleEffect;
import io.github.thewebcode.yplugin.effect.Particles;
import io.github.thewebcode.yplugin.entity.Entities;
import io.github.thewebcode.yplugin.exceptions.ProjectileCreationException;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.game.guns.BaseGun;
import io.github.thewebcode.yplugin.game.item.BaseWeapon;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.potion.Potions;
import io.github.thewebcode.yplugin.sound.Sounds;
import io.github.thewebcode.yplugin.time.TimeType;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class FlamingEnderSword extends BaseWeapon {

    private static FlamingEnderSword instance = null;

    public static FlamingEnderSword getInstance() {
        if (instance == null) {
            instance = new FlamingEnderSword();
            Gadgets.registerGadget(instance);

        }
        return instance;
    }

    private BaseGun enderGun = new BaseGun(ItemBuilder.of(Material.LEGACY_ENDER_STONE)) {

        @Override
        public void onFire(Player shooter) {

        }
    };

    protected FlamingEnderSword() {
        super(ItemBuilder.of(Material.WOODEN_SWORD).name("&2Sword of Enders").lore("&cScorch your foes!"));
        properties().droppable(true).breakable(false);

        enderGun.bulletProperties().speed(4).damage(5).damageCondition((shooter, target) -> target.getType() != EntityType.ENDERMAN);
        enderGun.getBulletBuilder().gun(enderGun).type(Material.ENDER_PEARL);
    }

    @Override
    public void onSwing(Player p) {
        Particles.sendToLocation(p.getLocation(), Particle.SUSPENDED_DEPTH, NumberUtil.getRandomInRange(1, 4));
        Particles.sendToLocation(Players.getTargetLocation(p, 30), Particle.SUSPENDED_DEPTH, NumberUtil.getRandomInRange(1, 4));
    }

    @Override
    public void onActivate(Player p) {
        try {
            enderGun.getBulletBuilder().shooter(p).shoot();
        } catch (ProjectileCreationException e) {
            Chat.message(p, "Unable to fire bullets at target. Projectile Creation Exception");
        }
    }

    @Override
    public void onAttack(Player p, LivingEntity e) {
        EntityType type = e.getType();
        if (type == EntityType.ENDERMAN) {
            Chat.message(p, "&eYou attacked an enderman! F0k, let's kill em!");
            Entities.kill(e);
            return;
        }

        Entities.burn(e, 1, TimeType.MINUTE);
    }

    @Override
    public void onBreak(Player p) {
        Chat.message(p, "Your sword of enders has broken");
    }

    @Override
    public void onDrop(Player p, Item item) {
        Sounds.playSound(p, Sound.ENTITY_ENDERMAN_STARE);
        Chat.message(p, "&7The dark-side isn't fond of that disrespect");
        Players.addPotionEffect(p, Potions.getPotionEffect(PotionEffectType.BLINDNESS, 1, 160));
        item.remove();
    }
}
