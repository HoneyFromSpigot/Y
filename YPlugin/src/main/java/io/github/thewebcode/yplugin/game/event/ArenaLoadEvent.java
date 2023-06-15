package io.github.thewebcode.yplugin.game.event;


import io.github.thewebcode.yplugin.game.MiniGame;
import io.github.thewebcode.yplugin.game.world.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaLoadEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    private Arena loaded;

    private MiniGame game;

    public ArenaLoadEvent(MiniGame game, Arena loaded) {
        this.game = game;
        this.loaded = loaded;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Arena getArena() {
        return loaded;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public MiniGame getGame() {
        return game;
    }
}
