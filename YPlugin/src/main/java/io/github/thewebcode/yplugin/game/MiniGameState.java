package io.github.thewebcode.yplugin.game;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.game.state.GameState;
import io.github.thewebcode.yplugin.game.state.IGameState;

public abstract class MiniGameState extends GameState {
    private boolean setup = false;

    public abstract void update();

    public abstract int id();

    public abstract boolean switchState();

    public abstract int nextState();

    public void setSetup(boolean val) {
        setup = val;
    }

    @Override
    public boolean isSetup() {
        return setup;
    }

    @Override
    public abstract void setup();

    public abstract void destroy();

    public void debug(String... text) {
        Chat.debug(text);
    }
}
