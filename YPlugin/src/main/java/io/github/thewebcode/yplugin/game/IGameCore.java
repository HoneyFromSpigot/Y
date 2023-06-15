package io.github.thewebcode.yplugin.game;


import io.github.thewebcode.yplugin.game.feature.FeatureManager;
import io.github.thewebcode.yplugin.game.feature.GameFeature;
import io.github.thewebcode.yplugin.game.players.UserManager;

public interface IGameCore<T extends UserManager> {

    void update();

    long tickDelay();

    T getUserManager();

    FeatureManager getFeatureManager();

    void registerFeatures(GameFeature... features);
}
