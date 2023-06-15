package io.github.thewebcode.yplugin.game.thread;

import io.github.thewebcode.yplugin.game.CraftGame;
import io.github.thewebcode.yplugin.game.IGameCore;
import io.github.thewebcode.yplugin.game.feature.FeatureManager;

public class GameUpdateThread implements Runnable {

    private IGameCore core;

    public GameUpdateThread(CraftGame core) {
        this.core = core;
    }

    @Override
    public void run() {
        core.update();
        FeatureManager features = core.getFeatureManager();
        if (features.hasFeatures()) {
            features.tickEnabled();
        }
    }
}
