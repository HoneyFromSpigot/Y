package io.github.thewebcode.yplugin.game;

import io.github.thewebcode.yplugin.game.clause.ServerShutdownClause;
import io.github.thewebcode.yplugin.game.feature.FeatureManager;
import io.github.thewebcode.yplugin.game.feature.GameFeature;
import io.github.thewebcode.yplugin.game.players.UserManager;
import io.github.thewebcode.yplugin.game.state.GameState;
import io.github.thewebcode.yplugin.game.state.GameStateManager;
import io.github.thewebcode.yplugin.game.thread.GameUpdateThread;
import io.github.thewebcode.yplugin.plugin.IYBukkitPlugin;

import java.util.HashSet;
import java.util.Set;

public abstract class CraftGame<T extends UserManager> extends IYBukkitPlugin implements IGameCore {

    private FeatureManager featureManager;

    private Set<ServerShutdownClause> shutdownClauses = new HashSet<>();

    private GameStateManager stateManager;

    @Override
    public void onEnable() {
        featureManager = new FeatureManager(this);
        stateManager = new GameStateManager(this);

        super.onEnable();
        GameUpdateThread updateThread = new GameUpdateThread(this);
        getThreadManager().registerSyncRepeatTask("Game Update", updateThread, 20, tickDelay());
    }

    public abstract void startup();

    public abstract void shutdown();

    public abstract String getAuthor();

    public abstract void initConfig();

    @Override
    public void update() {
        stateManager.update();
    }

    public abstract long tickDelay();

    public abstract T getUserManager();

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    public GameStateManager getStateManager() {
        return stateManager;
    }

    public void registerFeatures(GameFeature... features) {
        getFeatureManager().addFeatures(features);
        registerListeners(features);
    }

    public void registerGameState(GameState state) {
        getStateManager().addGameState(state);
    }


    public void registerGameStates(GameState... states) {
        for (GameState state : states) {
            registerGameState(state);
        }
    }

}
