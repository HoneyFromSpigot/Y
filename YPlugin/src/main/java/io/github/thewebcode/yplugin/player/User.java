package io.github.thewebcode.yplugin.player;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.chat.Title;
import io.github.thewebcode.yplugin.world.Worlds;
import io.github.thewebcode.yplugin.yml.Path;
import io.github.thewebcode.yplugin.yml.YamlConfig;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class User extends YamlConfig implements PlayerWrapper {
    @Path("name")
    private String name;

    @Path("uuid")
    private UUID id;

    @Path("world")
    private String worldName;

    public User(Player p) {
        name = p.getName();
        id = p.getUniqueId();
        worldName = p.getWorld().getName();
    }

    public User(String name, UUID id, String world) {
        this.name = name;
        this.id = id;
        this.worldName = world;
    }

    public User() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Player getPlayer() {
        return Players.getPlayer(id);
    }

    public boolean isOnline() {
        return Players.isOnline(getId());
    }

    public World getWorld() {
        return Worlds.getWorld(worldName);
    }

    @Deprecated
    public void updateWorld() {
        World playerWorld = getPlayer().getWorld();
        String playerWorldName = playerWorld.getName();
        if (worldName != null && playerWorldName.equals(worldName)) {
            return;
        }

        World oldWorld = Worlds.getWorld(worldName);
        worldName = playerWorld.getName();
        onWorldChange(oldWorld, playerWorld);
    }

    public void sync(Player player) {
        this.id = player.getUniqueId();
        this.name = player.getName();
        this.worldName = player.getWorld().getName();
    }

    public void destroy() {

    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void onWorldChange(World from, World to) {

    }

    public void message(String... messages) {
        Chat.message(getPlayer(), messages);
    }

    public void actionMessage(String message) {
        Chat.actionMessage(getPlayer(), message);
    }

    public void title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        getPlayer().sendTitle(Chat.format(title), Chat.format(subtitle), fadeIn, stay, fadeOut);
    }

    public void title(Title title) {
        title.send(getPlayer());
    }

    public void format(String msg, Object... args) {
        Chat.format(getPlayer(), msg, args);
    }
}
