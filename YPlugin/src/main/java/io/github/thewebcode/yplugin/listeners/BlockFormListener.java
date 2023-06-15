package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.config.Configuration;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class BlockFormListener implements Listener {

    private Configuration config;

    public BlockFormListener() {
        config = YPlugin.getInstance().getConfiguration();
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        Material blockType = event.getNewState().getType();
        switch (blockType) {
            case SNOW:
                if (config.disableSnowAccumulation()) {
                    event.setCancelled(true);
                }
                break;
            case ICE:
                if (config.disableIceAccumulation()) {
                    event.setCancelled(true);
                }
                break;
            default:
                break;
        }
    }
}
