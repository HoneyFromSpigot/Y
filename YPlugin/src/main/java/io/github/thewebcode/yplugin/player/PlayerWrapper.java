package io.github.thewebcode.yplugin.player;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface PlayerWrapper {
    public String getName();

    public UUID getId();

    public Player getPlayer();
}
