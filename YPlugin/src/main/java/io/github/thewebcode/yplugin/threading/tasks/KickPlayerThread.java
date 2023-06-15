package io.github.thewebcode.yplugin.threading.tasks;


import io.github.thewebcode.yplugin.player.Players;

import java.util.UUID;

public class KickPlayerThread implements Runnable {
    private UUID id;
    private String reason;

    public KickPlayerThread(UUID id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    @Override
    public void run() {
        Players.kick(Players.getPlayer(id), reason);
    }
}
