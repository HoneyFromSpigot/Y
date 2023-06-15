package io.github.thewebcode.yplugin.item;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.inventory.Inventories;
import io.github.thewebcode.yplugin.yml.InvalidConfigurationException;
import io.github.thewebcode.yplugin.yml.Path;
import io.github.thewebcode.yplugin.yml.YamlConfig;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemSetManager {

    private Map<String, ItemSet> itemSets = new HashMap<>();

    public ItemSetManager() {

    }

    public void addSet(ItemSet set) {
        itemSets.put(set.getName().toLowerCase(), set);
        save(set);
    }

    public void addSet(String name, Inventory inv) {
        ItemSet set = new ItemSet(name, inv);

        itemSets.put(name.toLowerCase(), set);
        save(set);
    }

    public boolean setExists(String name) {
        return itemSets.containsKey(name.toLowerCase());
    }

    public ItemSet getSet(String name) {
        return itemSets.get(name.toLowerCase());
    }

    public Set<String> getSetNames() {
        return itemSets.keySet();
    }

    private void save(ItemSet set) {
        String fileName = String.format("%s%s.yml", YPlugin.ITEM_SET_DATA_FOLDER, set.getName());
        File itemSetFile = new File(fileName);

        try {
            set.save(itemSetFile);
            Chat.debug(String.format("Saved item-set %s to file", set.getName()));
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            Chat.debug("Failed to save item set. Please check the console for error log");
        }
    }

    public static class ItemSet extends YamlConfig {
        @Path("name")
        private String setName;

        @Path("inventory")
        private Inventory inventory;

        public ItemSet(File file) {
            super(file);
        }

        public ItemSet(String name, Inventory inv) {
            this.setName = name;
            this.inventory = inv;
        }

        public Map<Integer, ItemStack> getInventoryContents() {
            return Inventories.getContentsAsMap(this.inventory);
        }

        public String getName() {
            return setName;
        }
    }

}
