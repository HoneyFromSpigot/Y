package io.github.thewebcode.yplugin.game.players;

import io.github.thewebcode.yplugin.player.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.UUID;

public interface IUserManager<T extends User> {
    void addUser(Player p);

    T getUser(Player p);

    T getUser(UUID id);

    void removeUser(Player p);

    void removeUser(UUID id);

    Collection<T> allUsers();

    JavaPlugin getParent();

    void setParent(JavaPlugin plugin);
}
