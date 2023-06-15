package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.FlagArg;
import io.github.thewebcode.yplugin.command.Flags;
import io.github.thewebcode.yplugin.inventory.HandSlot;
import io.github.thewebcode.yplugin.item.Enchantments;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCommand {
    @Command(identifier = "enchant", permissions = Perms.COMMAND_ENCHANT)
    @Flags(identifier = {"o", "-both"})
    public void enchantCommand(Player player, @Arg(name = "enchantment") Enchantment enchant, @Arg(name = "level") int level, @FlagArg("o") final boolean offHand, @FlagArg("-both") final boolean bothHands) {
        if (bothHands && (!Players.hasItemInHand(player, HandSlot.MAIN_HAND) || !Players.hasItemInHand(player, HandSlot.OFF_HAND))) {
            Chat.message(player, Messages.ITEM_IN_BOTH_HANDS_REQUIRED);
            return;
        }
        if (!Players.hasItemInHand(player)) {
            Chat.message(player, Messages.ITEM_IN_EITHER_HAND_REQUIRED);
            return;
        }

        if (offHand && !Players.hasItemInHand(player, HandSlot.OFF_HAND)) {
            Chat.message(player, Messages.ITEM_IN_HAND_REQUIRED);
            return;
        }
        if (bothHands) {
            ItemStack mainHand = Players.getItemInHand(player, HandSlot.MAIN_HAND);
            ItemStack offHandItem = Players.getItemInHand(player, HandSlot.OFF_HAND);

            if (!Items.addEnchantment(mainHand, enchant, level, true)) {
                Chat.message(player, Messages.failedToEnchantItem(HandSlot.MAIN_HAND));
            } else {
                Chat.message(player, Messages.itemEnchantmentAdded(enchant.getName(), level, HandSlot.MAIN_HAND));
            }

            if (!Items.addEnchantment(offHandItem, enchant, level, true)) {
                Chat.message(player, Messages.failedToEnchantItem(HandSlot.OFF_HAND));
            } else {
                Chat.message(player, Messages.itemEnchantmentAdded(enchant.getName(), level, HandSlot.OFF_HAND));
            }

        } else {
            HandSlot slot = offHand ? HandSlot.OFF_HAND : HandSlot.MAIN_HAND;

            ItemStack hand = Players.getItemInHand(player, slot);
            if (!Items.addEnchantment(hand, enchant, level, true)) {
                Chat.message(player, Messages.failedToEnchantItem(slot));
                return;
            }
            Chat.message(player, Messages.itemEnchantmentAdded(enchant.getName(), level, slot));
        }
    }

    @Command(identifier = "enchant list", permissions = "commons.command.enchant")
    public void enchantListCommand(Player player) {
        for (Enchantments enchants : Enchantments.values()) {
            Chat.message(player, String.format("&eIdentifier: &a%s\n&r&e- Aliases:\n&a%s", enchants.getMainAlias(), StringUtil.joinString(enchants.getAliases(), ", ")));
        }
    }
}
