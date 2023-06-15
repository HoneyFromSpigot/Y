package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.entity.Player;

public class SpawnCommand {
    @Command(identifier = "spawn", permissions = Perms.COMMAND_SPAWN)
    public void onSpawnCommand(Player player) {
        Players.teleport(player, Worlds.getSpawn(player.getWorld()));
    }
}
