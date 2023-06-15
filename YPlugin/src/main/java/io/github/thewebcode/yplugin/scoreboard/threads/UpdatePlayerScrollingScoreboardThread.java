package io.github.thewebcode.yplugin.scoreboard.threads;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardEntry;
import io.github.thewebcode.yplugin.scoreboard.scrolling.ScrollingScoreboardEntry;
import io.github.thewebcode.yplugin.scoreboard.scrolling.ScrollingScoreboardWrapper;
import io.github.thewebcode.yplugin.utilities.TextCycler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UpdatePlayerScrollingScoreboardThread extends BukkitRunnable {
    private boolean populated;

    private static final YPlugin commons = YPlugin.getInstance();

    private Map<ScrollingScoreboardEntry, IndexedTextCycler> indexedEntryCyclers = new HashMap<>();
    private Map<String, Team> prefixesToTeams = new HashMap<>();

    private ScrollingScoreboardWrapper wrapper;

    public UpdatePlayerScrollingScoreboardThread(ScrollingScoreboardWrapper wrapper) {
        this.wrapper = wrapper;
    }

    private void init() {
        indexedEntryCyclers.clear();
        prefixesToTeams.clear();

        Collection<ScoreboardEntry> entries = wrapper.getInfo().getEntries();

        int entryCount = entries.size();

        commons.debug("Entries count = " + entryCount);

        Scoreboard scoreboard = wrapper.getScoreboard();
    }

    @Override
    public void run() {
    }

    public static class IndexedTextCycler {
        private TextCycler cycler;
        private int index;

        public IndexedTextCycler(int index, TextCycler cycler) {
            this.index = index;
            this.cycler = cycler;
        }

        public int getIndex() {
            return index;
        }

        public TextCycler getCycler() {
            return cycler;
        }

        public void setCycler(TextCycler cycler) {
            this.cycler = cycler;
        }
    }
}
