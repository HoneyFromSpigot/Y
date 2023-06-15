package io.github.thewebcode.yplugin.config;

import io.github.thewebcode.yplugin.yml.Path;
import io.github.thewebcode.yplugin.yml.YamlConfig;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class SerializableItemStack extends YamlConfig {
    @Path("item")
    private ItemStack item;

    public SerializableItemStack(File file) {
        super(file);
    }

    public SerializableItemStack(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
