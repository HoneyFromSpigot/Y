package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.config.SerializableItemStack;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.yml.InvalidConfigurationException;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class DebugItemDeserialize implements DebugAction {

    @Override
    public void doAction(Player player, String... args) {
        File debugFolder = new File(YPlugin.DEBUG_DATA_FOLDER);

        if (!debugFolder.exists()) {
            debugFolder.mkdirs();
        }

        Set<ItemStack> deserializedItems = new HashSet<>();

        for (File file : FileUtils.listFiles(debugFolder, null, false)) {

            SerializableItemStack item = new SerializableItemStack(file);

            try {
                item.load();
                debug(String.format("Item '%s' loaded", Items.getName(item.getItem())));
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
                debug(String.format("Error loading item from file: '%s'", file.getName()));
                continue;
            }

            deserializedItems.add(item.getItem());
            Chat.message(player,Messages.itemInfo(item.getItem()));
        }

        if (deserializedItems.size() > 0) {
            for (ItemStack item : deserializedItems) {
                Players.giveItem(player, item, true);
            }
        }
    }

    @Override
    public String getActionName() {
        return "item_deserialize";
    }
}
