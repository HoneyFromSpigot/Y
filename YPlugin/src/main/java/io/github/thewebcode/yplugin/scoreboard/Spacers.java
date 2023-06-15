package io.github.thewebcode.yplugin.scoreboard;

import io.github.thewebcode.yplugin.scoreboard.scrolling.ScrollingScoreboardEntry;
import org.bukkit.ChatColor;

@Deprecated
public final class Spacers {
    private Spacers() {
    }

    @Deprecated
    public static final String FIRST = " ";
    @Deprecated
    public static final String SECOND = ChatColor.RESET.toString() + " ";
    @Deprecated
    public static final String THIRD = ChatColor.RESET.toString() + ChatColor.RESET.toString() + " ";
    @Deprecated
    public static final String FOURTH = ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + " ";
    @Deprecated
    public static final String FIFTH = ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + " ";
    @Deprecated
    public static final String SIXTH = ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + " ";
    @Deprecated
    public static final String SEVENTH = ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + " ";
    @Deprecated
    public static final String EIGHTH = ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + " ";

    private static final String[] SPACERS = new String[]{
            FIRST,
            SECOND,
            THIRD,
            FOURTH,
            FIFTH,
            SIXTH,
            SEVENTH,
            EIGHTH,
    };

    @Deprecated
    public static String getSpacer(int number) {
        if (number < 1 || number >= 8) {
            return null;
        }

        return SPACERS[number - 1];
    }

    @Deprecated
    public static ScrollingScoreboardEntry createEntry(String spacer) {
        return new ScrollingScoreboardEntry(spacer);
    }
}
