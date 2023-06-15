package io.github.thewebcode.yplugin.game.players;

import io.github.thewebcode.yplugin.game.event.UserJoinEvent;
import io.github.thewebcode.yplugin.game.event.UserQuitEvent;
import io.github.thewebcode.yplugin.player.User;
import io.github.thewebcode.yplugin.plugin.Plugins;
import io.github.thewebcode.yplugin.reflection.ReflectionUtilities;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager<T extends User> implements IUserManager<T> {
    private Map<UUID, T> users = new HashMap<>();

    private Class<? extends User> playerClass = null;

    private Constructor userContructor = null;

    private JavaPlugin parent = null;

    public UserManager(Class<? extends User> userClass) {
        setUserClass(userClass);
    }

    public UserManager() {

    }

    public void setParent(JavaPlugin plugin) {
        this.parent = plugin;
    }

    public JavaPlugin getParent() {
        return parent;
    }

    public void addUser(Player p) {
        T userObject = ReflectionUtilities.invokeConstructor(userContructor, p);
        users.put(p.getUniqueId(), userObject);
        callUserJoin(userObject);
    }

    public void addUser(T user) {
        users.put(user.getId(), user);
        callUserJoin(user);
    }

    public void disposeAll() {
        if (users.isEmpty()) {
            return;
        }

        for (T user : users.values()) {
            callUserQuit(user);
            user.destroy();
        }
    }

    public T getUser(Player p) {
        return getUser(p.getUniqueId());
    }

    public T getUser(UUID id) {
        return users.get(id);
    }

    public void removeUser(Player p) {
        removeUser(p.getUniqueId());
    }

    public void removeUser(UUID id) {
        remove(id);
    }

    public boolean hasData(UUID id) {
        return users.containsKey(id);
    }

    protected void put(UUID id, T user) {
        users.put(id, user);
    }

    protected void remove(UUID id) {
        T user = getUser(id);
        callUserQuit(user);
        users.remove(id);
    }

    public void setUserClass(Class<? extends User> userClass) {
        this.playerClass = userClass;
        userContructor = ReflectionUtilities.getConstructor(playerClass, Player.class);
    }

    public Class<? extends User> getUserClass() {
        return playerClass;
    }

    public boolean hasUserClass() {
        return playerClass != null && userContructor != null;
    }

    public Collection<T> allUsers() {
        return users.values();
    }

    public boolean hasUsers() {
        return users.size() > 0;
    }

    protected void callUserJoin(T userObject) {
        UserJoinEvent userJoinEvent = new UserJoinEvent(getParent(), userObject);
        Plugins.callEvent(userJoinEvent);
    }

    protected void callUserQuit(T userObject) {
        UserQuitEvent userQuitEvent = new UserQuitEvent(getParent(), userObject);
        Plugins.callEvent(userQuitEvent);
    }
}
