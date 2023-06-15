package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.scoreboard.BoardManager;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardInformation;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardType;
import io.github.thewebcode.yplugin.scoreboard.ScoreboardWrapper;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.entity.Player;

public class DebugDefaultScoreboard implements DebugAction {
    private static final YPlugin commons = YPlugin.getInstance();

    private static BoardManager manager = commons.getScoreboardManager();

    private ScoreboardWrapper wrapper = manager.builder().title("&cDefault board!").dummyObjective().type(ScoreboardType.NORMAL).entry(1, "Line uno").entry(2, "Line deus").build();

    private boolean inited = false;

    @Override
    public void doAction(Player player, String... args) {
        if (!inited) {
            manager.setDefaultScoreboard(wrapper);
            inited = true;
            return;
        }

        ScoreboardInformation info = wrapper.getInfo();
        for (int i = 0; i < args.length; i++) {
            info.entry(i + 1, args[i]);
        }

        commons.debug("Assigned entries " + StringUtil.joinString(args, ", ") + " to default wrapper");
    }

    @Override
    public String getActionName() {
        return "default_scoreboard";
    }
}
