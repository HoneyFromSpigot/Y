package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DebugHandItem implements DebugAction {

    @Override
    public void doAction(Player player, String... args) {
        if (!Players.hasItemInHand(player)) {
            Chat.message(player, Messages.DEBUG_ACTION_REQUIRES_HAND_ITEM);
            return;
        }

        ItemStack itemStack = player.getItemInHand();
        Chat.message(player, Messages.itemInfo(itemStack));
    }

    @Override
    public String getActionName() {
        return "hand_item_info";
    }
}
