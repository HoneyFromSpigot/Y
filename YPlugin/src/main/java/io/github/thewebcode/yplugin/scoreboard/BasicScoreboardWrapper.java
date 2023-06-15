package io.github.thewebcode.yplugin.scoreboard;

import com.google.common.collect.Sets;
import io.github.thewebcode.yplugin.scoreboard.threads.UpdateBasicScoreboardThread;
import lombok.NonNull;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.Set;

public class BasicScoreboardWrapper extends AbstractScoreboardWrapper {

    private Set<BukkitRunnable> updateThreads;

    public BasicScoreboardWrapper(@NonNull BoardManager manager, @NonNull Scoreboard board, @NonNull ScoreboardInformation info) {
        super(manager, board, info);
        updateThreads = Sets.newHashSet(new UpdateBasicScoreboardThread(this));
    }

    @Override
    public boolean hasThreads() {
        return true;
    }

    @Override
    public Collection<? extends Runnable> getThreads() {
        return updateThreads;
    }
}
