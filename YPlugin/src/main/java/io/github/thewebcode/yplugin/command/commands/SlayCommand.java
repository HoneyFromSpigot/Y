package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.FlagArg;
import io.github.thewebcode.yplugin.command.Flags;
import io.github.thewebcode.yplugin.entity.Entities;
import io.github.thewebcode.yplugin.permission.Perms;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Set;

public class SlayCommand {
    @Command(identifier = "slay", permissions = Perms.COMMAND_SLAY)
    @Flags(identifier = {"p"}, description = {"Whether or not to slay the players aswell."})
    public void onSlayCommand(Player player, @FlagArg("p") boolean killPlayers, @Arg(name = "radius") int radius) {
        int amountRemoved = 0;
        Set<LivingEntity> entities = Entities.getLivingEntitiesNearLocation(player.getLocation(), radius);
        for (LivingEntity entity : entities) {
            if (!killPlayers && entity instanceof Player) {
                continue;
            }
            Entities.kill(entity);
            amountRemoved++;
        }
        Chat.message(player, Messages.entityRemovedEntities(amountRemoved));
    }
}
