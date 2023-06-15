package io.github.thewebcode.yplugin.threading.tasks;

import io.github.thewebcode.yplugin.player.Players;

import java.util.UUID;
import java.util.concurrent.Callable;

public class GetPlayerNameCallable implements Callable<String> {
    private UUID uniqueId;

    public GetPlayerNameCallable(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String call() throws Exception {
        return Players.getNameFromUUID(uniqueId);
    }
}
