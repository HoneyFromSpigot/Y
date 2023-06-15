package io.github.thewebcode.yplugin.scoreboard;

import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.plugin.IYBukkitPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public interface BoardManager {
    public static final String SIDEBOARD_OBJECTIVE_NAME = "scoreboard";

    public default ScoreboardWrapper getDefaultWrapper() {
        return null;
    }

    public default boolean hasDefaultScoreboard() {
        return false;
    }

    public default void setDefaultScoreboard(ScoreboardWrapper wrapper) {

    }

    public boolean hasData();

    public default boolean hasData(Player p) {
        return hasData(p.getUniqueId());
    }

    public boolean hasData(UUID id);

    public ScoreboardWrapper getWrapper(UUID id);

    public default ScoreboardWrapper getWrapper(Player p) {
        return getWrapper(p.getUniqueId());
    }

    public default ScoreboardInformation getData(Player p) {
        return getData(p.getUniqueId());
    }

    public ScoreboardInformation getData(UUID id);

    public default Scoreboard getScoreboard(Player p) {
        return getScoreboard(p.getUniqueId());
    }

    public Scoreboard getScoreboard(UUID id);

    public void resetScoreboard(Player p);

    public default void resetScoreboard(UUID id) {
        resetScoreboard(Players.getPlayer(id));
    }

    public IYBukkitPlugin getPlugin();

    public ScoreboardBuilder builder();

    public void assign(Player player, ScoreboardWrapper wrapper);

    public ScoreboardWrapper bake(ScoreboardType type, ScoreboardInformation scoreboardInformation, ObjectiveRegisterData objectiveRegisterData);
}
