package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.FlagArg;
import io.github.thewebcode.yplugin.command.Flags;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class RepairCommand {
    @Command(identifier = "repair", permissions = Perms.COMMAND_REPAIR)
    @Flags(identifier = "a", description = "Repair all your items, not just the one in your hand")
    public void onItemRepairCommand(Player player, @FlagArg("a") final boolean all) {
        if (all) {
            Players.repairItems(player, true);
        }

        if (!all) {
            if (Players.handIsEmpty(player)) {
                Chat.message(player, Messages.ITEM_IN_HAND_REQUIRED);
                return;
            }

            Items.repairItem(player.getItemInHand());
        }

        Chat.message(player, Messages.ITEMS_REPAIRED);
    }
}
