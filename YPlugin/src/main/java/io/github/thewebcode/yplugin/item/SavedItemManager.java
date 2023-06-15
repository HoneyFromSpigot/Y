package io.github.thewebcode.yplugin.item;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.config.SerializableItemStack;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SavedItemManager {

    private static final Map<String, ItemStack> items = new HashMap<>();

    public static Set<String> getItemNames() {
        return items.keySet();
    }

    public static boolean saveItem(String name, ItemStack item) {
        if (items.containsKey(name)) {
            return false;
        }
        SerializableItemStack serialItem = new SerializableItemStack(item);
        File itemFile = new File(String.format("%s/%s.xml", YPlugin.ITEM_DATA_FOLDER, name));

        boolean saved = true;

        try {
            serialItem.save(itemFile);

            items.put(name, item);
        } catch (Exception e) {
            e.printStackTrace();
            saved = true;
        }

        return saved;
    }

    public static void loadItem(File file) {
        String itemName = FilenameUtils.removeExtension(file.getName());

        SerializableItemStack fileItem = new SerializableItemStack(file);
        try {
            fileItem.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemStack item = fileItem.getItem();

        if (item == null) {
            return;
        }

        items.put(itemName, item);
        Chat.debug(String.format("Loaded item %s", StringUtil.joinString(Messages.itemInfo(item), "\n", 0)));
    }

    public static ItemStack getItem(String name) {
        for (Map.Entry<String, ItemStack> itemEntry : items.entrySet()) {
            String entryName = itemEntry.getKey();
            if (!name.equalsIgnoreCase(entryName)) {
                continue;
            }

            return itemEntry.getValue();
        }

        return null;
    }


}
