package io.github.thewebcode.yplugin.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface CreatureHitAction {
    void onHit(Player player, LivingEntity entity);
}
