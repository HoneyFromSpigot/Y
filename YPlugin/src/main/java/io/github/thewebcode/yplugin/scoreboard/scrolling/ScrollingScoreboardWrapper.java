package io.github.thewebcode.yplugin.scoreboard.scrolling;

import com.google.common.collect.Lists;
import io.github.thewebcode.yplugin.scoreboard.AbstractScoreboardWrapper;
import io.github.thewebcode.yplugin.scoreboard.BoardManager;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardInformation;
import io.github.thewebcode.yplugin.scoreboard.threads.UpdatePlayerScrollingScoreboardThread;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.UUID;

public class ScrollingScoreboardWrapper extends AbstractScoreboardWrapper {
    private Scoreboard scoreboard;

    private DisplaySlot slot;

    private List<BukkitRunnable> threads = Lists.newArrayListWithExpectedSize(2);

    public ScrollingScoreboardWrapper(@NonNull BoardManager manager, @NonNull Scoreboard scoreboard, @NonNull ScoreboardInformation info) {
        super(manager, scoreboard, info);
    }

    @Override
    public ScrollingScoreboardWrapper assign(Player p) {
        UUID id = p.getUniqueId();

        Objective obj = scoreboard.getObjective(slot);

        obj.setDisplayName(getInfo().getTitle().toString());
        threads.add(new UpdatePlayerScrollingScoreboardThread(this));
        p.setScoreboard(scoreboard);
        return this;
    }

    public List<BukkitRunnable> getThreads() {
        return threads;
    }
}
