package io.github.thewebcode.yplugin.nms.nonbreaking;

import io.github.thewebcode.yplugin.nms.ForceRespawnHandler;
import org.bukkit.entity.Player;

import java.util.ConcurrentModificationException;

public class NonBreakingForceRespawnHandler implements ForceRespawnHandler {
    @Override
    public void forceRespawn(Player player) throws ConcurrentModificationException {
        try {
            player.spigot().respawn();
        } catch (Exception e) {
            throw new IllegalAccessError("Unable to perform spigot().respawn() on player. Are you using Spigot?");
        }
    }
}
