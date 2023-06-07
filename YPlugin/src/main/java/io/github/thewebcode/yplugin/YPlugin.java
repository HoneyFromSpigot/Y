package io.github.thewebcode.yplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class YPlugin extends JavaPlugin {
    private static YPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static YPlugin getInstance() {
        return instance;
    }
}
