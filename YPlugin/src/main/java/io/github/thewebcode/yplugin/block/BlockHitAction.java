package io.github.thewebcode.yplugin.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface BlockHitAction {
    void onHit(Player player, Block block);
}
