package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.FlagArg;
import io.github.thewebcode.yplugin.command.Flags;
import io.github.thewebcode.yplugin.inventory.HandSlot;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MoreCommand {
    @Command(identifier = "more", permissions = {Perms.COMMAND_MORE})
    @Flags(identifier = {"a", "o", "-hands"})
    public void onMoreCommand(Player player, @FlagArg("a") final boolean allItems, @FlagArg("o") final boolean offHand, @FlagArg("-hands") final boolean hands) {
        if (allItems) {
            PlayerInventory inventory = player.getInventory();
            for (ItemStack item : inventory.getContents()) {
                if (item == null) {
                    continue;
                }

                item.setAmount(item.getMaxStackSize());
            }
            return;
        }

        if (!Players.hasItemInHand(player)) {
            Chat.message(player, Messages.ITEM_IN_HAND_REQUIRED);
            return;
        }
        if (hands) {
            if (!Players.handIsEmpty(player, HandSlot.MAIN_HAND)) {
                ItemStack mainHandItem = Players.getItemInHand(player, HandSlot.MAIN_HAND);
                mainHandItem.setAmount(mainHandItem.getMaxStackSize());
                Players.setItemInHand(player, mainHandItem, HandSlot.MAIN_HAND);
            }

            if (!Players.handIsEmpty(player, HandSlot.OFF_HAND)) {
                ItemStack offHandItem = Players.getItemInHand(player, HandSlot.OFF_HAND);
                offHandItem.setAmount(offHandItem.getMaxStackSize());
                Players.setItemInHand(player, offHandItem, HandSlot.OFF_HAND);
            }

            return;
        }

        if (offHand) {
            if (Players.handIsEmpty(player, HandSlot.OFF_HAND)) {
                Chat.message(player, Messages.ITEM_IN_HAND_REQUIRED);
                return;
            }

            ItemStack offHandItem = Players.getItemInHand(player, HandSlot.OFF_HAND);
            offHandItem.setAmount(offHandItem.getMaxStackSize());
            Players.setItemInHand(player, offHandItem, HandSlot.OFF_HAND);
        }

        if (Players.handIsEmpty(player, HandSlot.MAIN_HAND)) {
            Chat.message(player, Messages.ITEM_IN_HAND_REQUIRED);
            return;
        }

        ItemStack mainHandItem = Players.getItemInHand(player, HandSlot.MAIN_HAND);
        mainHandItem.setAmount(mainHandItem.getMaxStackSize());
        Players.setItemInHand(player, mainHandItem, HandSlot.MAIN_HAND);

    }
}
