package io.github.thewebcode.yplugin.game.world;

import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public interface ArenaHandler {

    Arena getArena(String worldName);

    boolean addArena(Arena arena);

    void cycleArena();

    void setActiveArena(Arena arena);

    Arena getActiveArena();

    void loadArena(Arena arena);

    void unloadArena(Arena arena);

    void removeArena(Arena arena);

    boolean hasArenas();

    static void teleportToRandomSpawn(Player player, Arena arena) {
        if (player == null || arena == null) {
            return;
        }
        Players.teleport(player, arena.getRandomSpawn());
    }
}
