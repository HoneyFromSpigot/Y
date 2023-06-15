package io.github.thewebcode.yplugin.game.state;

import org.bukkit.event.Listener;

public interface IGameState extends Listener {
    default void setup() {

    }

    default boolean isSetup() {
        return false;
    }

    void setSetup(boolean val);

    default void destroy() {

    }

    void update();

    int id();

    boolean switchState();

    int nextState();
}
