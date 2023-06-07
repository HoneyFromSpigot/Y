package io.github.thewebcode.yplugin;

import io.github.thewebcode.yplugin.event.Eventlistener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class YPlugin extends JavaPlugin {
    private static YPlugin instance;

    @Override
    public void onEnable() {
        instance = this;


        registerEvents();
    }

    private void registerEvents(){
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new Eventlistener(), this);
    }

    public static YPlugin getInstance() {
        return instance;
    }
}
