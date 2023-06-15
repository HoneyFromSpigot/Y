package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.scoreboard.BoardManager;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardInformation;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardType;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardWrapper;
import org.bukkit.entity.Player;

public class DebugScoreboardBuilder implements DebugAction {
    private static BoardManager manager = YPlugin.getInstance().getScoreboardManager();
    private ScoreboardWrapper wrapper;

    private boolean inited = false;

    @Override
    public void doAction(Player player, String... args) {
        if (!inited) {
            wrapper = manager.builder().type(ScoreboardType.NORMAL).title("&eTest").dummyObjective()
                    .entry(1, "Line 1?").entry(2, "Line 2?").entry(3, "Line 3?").build();

            manager.assign(player, wrapper);
            manager.getPlugin().debug("Assigned scoreboard wrapper to player" + player.getName());
            inited = true;
            return;
        }

        ScoreboardInformation info = wrapper.getInfo();

        for (int i = 0; i < args.length; i++) {
            info.entry(i + 1, args[i]);
        }

        manager.getPlugin().debug("Updated entries for the scoreboard.");
    }

    @Override
    public String getActionName() {
        return "scoreboard_builder";
    }
}
