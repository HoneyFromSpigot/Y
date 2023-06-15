package io.github.thewebcode.yplugin.game.guns;

import io.github.thewebcode.yplugin.effect.ParticleEffect;
import io.github.thewebcode.yplugin.effect.Particles;
import io.github.thewebcode.yplugin.game.clause.BulletDamageEntityClause;
import io.github.thewebcode.yplugin.time.BasicTicker;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Bullet extends BaseBullet {

    private BasicTicker ticker = new BasicTicker(7);

    public Bullet(UUID shooter, Gun gun, ItemStack item, double force, double damage, double spread, BulletDamageEntityClause damageConditions) {
        super(shooter, gun, item, force, damage, spread, damageConditions);
    }

    public Bullet(UUID shooter, Gun gun, ItemStack item, double force, double damage, double spread) {
        super(shooter, gun, item, force, damage, spread);
    }

    public Bullet(Player shooter, Gun gun, ItemStack item, double force, double damage, double spread) {
        super(shooter, gun, item, force, damage, spread);
    }

    @Override
    public void onTravel(Location l) {
        if (ticker.allow()) {
            Particles.sendToLocation(l, Particle.SPELL_INSTANT, 2);
            ticker.reset();
        } else {
            ticker.tick();
        }
    }
}
