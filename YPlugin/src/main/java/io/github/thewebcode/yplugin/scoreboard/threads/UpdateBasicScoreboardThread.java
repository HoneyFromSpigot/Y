package io.github.thewebcode.yplugin.scoreboard.threads;

import io.github.thewebcode.yplugin.scoreboard.ScoreboardEntry;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardWrapper;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

public class UpdateBasicScoreboardThread extends BukkitRunnable {
    private ScoreboardWrapper wrapper;

    private boolean populated;

    public UpdateBasicScoreboardThread(ScoreboardWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void run() {
        Scoreboard scoreboard = wrapper.getScoreboard();

        Collection<ScoreboardEntry> entries = wrapper.getInfo().getEntries();

        Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (!populated) {
            entries.forEach(e -> {
                obj.getScore(e.getValue()).setScore(e.getScore());
            });
            populated = true;
        }

        for (ScoreboardEntry entry : entries) {
            if (!entry.hasChanged()) {
                continue;
            }
            String oldText = entry.getPreviousValue();
            scoreboard.resetScores(oldText);
            obj.getScore(entry.getValue()).setScore(entry.getScore());
            entry.setChanged(false);
        }

    }
}
