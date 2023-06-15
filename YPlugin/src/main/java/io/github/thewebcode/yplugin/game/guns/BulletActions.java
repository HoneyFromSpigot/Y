package io.github.thewebcode.yplugin.game.guns;

import io.github.thewebcode.yplugin.block.BlockHitAction;
import io.github.thewebcode.yplugin.entity.CreatureHitAction;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface BulletActions extends CreatureHitAction, BlockHitAction {
    void onHit(Player player, LivingEntity target);

    void onHit(Player player, Block block);
}
