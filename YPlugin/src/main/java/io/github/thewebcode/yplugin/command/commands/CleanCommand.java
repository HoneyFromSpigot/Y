package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.FlagArg;
import io.github.thewebcode.yplugin.command.Flags;
import io.github.thewebcode.yplugin.entity.Entities;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.world.Worlds;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Set;

;

public class CleanCommand {

    @Command(identifier = "clean", permissions = Perms.COMMAND_CLEAN)
    public void onCleanCommand(Player player) {
        Chat.message(
                player,
                "&6There's several actions that can be performed",
                "&e- &aCleaning dropped items",
                "&e    *&b /clean items (radius)",
                "&e    *&b /clean items --all"
        );
    }

    @Command(identifier = "clean items", permissions = Perms.COMMAND_CLEAN_ENTITIES)
    @Flags(identifier = "-all")
    public void onCleanItemsCommand(Player p, @Arg(name = "radius", def = "0") int radius, @FlagArg("-all") boolean all) {
        if (!all && radius < 1) {
            Chat.message(p,
                    "&6Proper usage for &e/clean items&6 is:",
                    "&e    *&b /clean items (radius)",
                    "&e    *&b /clean items --all"
            );
            return;
        }

        int cleaned = 0;

        if (all) {
            cleaned = Worlds.clearDroppedItems(p.getWorld());
        } else {
            cleaned = Worlds.clearDroppedItems(p.getLocation(), radius);
        }

        Chat.message(p, String.format("&eCleaned &a%s&e dropped items!", cleaned));
    }

    @Command(identifier = "clean mobs", permissions = Perms.COMMAND_CLEAN_MOBS)
    @Flags(identifier = {"a", "-world"})
    public void onCleanMobCommand(Player p, @Arg(name = "radius", def = "0") int radius, @FlagArg("a") boolean all, @FlagArg("-world") @Arg(name = "world", def = "?sender") World world) {
        boolean sameWorld = p.getWorld().getName().equals(world.getName());
        if (!all && (sameWorld && radius < 1)) {
            Chat.message(p, "&6Proper usage for &e/clean mobs&6 is:",
                    "&e    *&b /clean mobs (radius)",
                    "&e    *&b /clean mobs --world (world)"
            );
            return;
        }

        int slayed = 0;

        if (all) {
            slayed = Worlds.cleanAllEntities(world);
        } else {
            if (radius < 1) {
                Chat.message(p, Messages.invalidCommandUsage("radius"));
                return;
            }

            Set<LivingEntity> entities = Entities.getLivingEntitiesNear(p, radius);

            for (LivingEntity mob : entities) {
                mob.remove();
                slayed++;
            }
        }

        Chat.message(p, String.format("&eRemoved &a%s&e mobs", slayed));
    }
}
