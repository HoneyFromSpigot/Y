package io.github.thewebcode.yplugin.game.listener;

import io.github.thewebcode.yplugin.game.MiniGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class ArenaLoadListener implements Listener {
    private MiniGame miniGame;

    public ArenaLoadListener(MiniGame game) {
        this.miniGame = game;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        miniGame.getArenaManager().addArena(e.getWorld());
    }
}
