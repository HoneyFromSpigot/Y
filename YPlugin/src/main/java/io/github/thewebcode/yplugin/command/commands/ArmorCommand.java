package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.item.ArmorSet;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class ArmorCommand {

    @Command(
            identifier = "armor",
            onlyPlayers = true,
            description = "Equip yourself with a full set of armor!",
            permissions = {Perms.COMMAND_ARMOR}
    )
    public void armorCommand(Player sender, @Arg(name = "armor_type") String armorType) {
        ArmorSet set = ArmorSet.getSetByName(armorType);
        if (set == null) {
            Chat.message(sender, Messages.invalidArmorSet(armorType));
            return;
        }

        Players.setArmor(sender, set.getArmor());

        Chat.actionMessage(sender,"&aEnjoy your &enew&a armor!");
    }
}
