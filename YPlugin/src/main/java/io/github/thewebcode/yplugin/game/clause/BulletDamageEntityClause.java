package io.github.thewebcode.yplugin.game.clause;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface BulletDamageEntityClause {

    boolean damage(Player shooter, LivingEntity target);
}
