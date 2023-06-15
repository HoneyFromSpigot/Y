package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.config.SerializableItemStack;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.yml.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DebugHandItemSerialize implements DebugAction {

    @Override
    public void doAction(Player player, String... args) {
        if (!Players.hasItemInHand(player)) {
            Chat.message(player, Messages.DEBUG_ACTION_REQUIRES_HAND_ITEM);
            return;
        }


        ItemStack playerHand = player.getItemInHand();
        Items.setName(playerHand, "&bThe Debugger");
        Items.setLore(playerHand, Arrays.asList("&eDebugging le hand item", "&6By adding lines of lore"));
        File itemFile = new File(String.format(YPlugin.DEBUG_DATA_FOLDER + "%s.yml", String.valueOf(System.currentTimeMillis())));

        if (!itemFile.exists()) {
            try {
                if (itemFile.createNewFile()) {
                    debug("Created item file at " + itemFile.getPath());
                } else {
                    debug("Failed to create item file at " + itemFile.getPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SerializableItemStack serialItem = new SerializableItemStack(playerHand);
        try {
            serialItem.save(itemFile);
            debug("Item saved to file successfully!");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            debug("Failed to save file. Check console for error log");
        }
    }

    @Override
    public String getActionName() {
        return "hand_item_serialize";
    }
}
