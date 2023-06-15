package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.potion.PotionType;
import io.github.thewebcode.yplugin.potion.Potions;
import org.bukkit.entity.Player;

public class PotionCommand {
    @Command(identifier = "potion", permissions = Perms.COMMAND_POTION)
    public void onPotionCommand(Player player, @Arg(name = "type") String potionType, @Arg(name = "level", def = "1") int effectLevel) {
        if (!PotionType.isPotionType(potionType)) {
            Chat.message(player, Messages.INVALID_POTION_TYPE);
            return;
        }

        Players.addPotionEffect(player, Potions.getPotionEffect(PotionType.getPotionType(potionType).getPotionEffectType(), effectLevel, Integer.MAX_VALUE));
    }
}
