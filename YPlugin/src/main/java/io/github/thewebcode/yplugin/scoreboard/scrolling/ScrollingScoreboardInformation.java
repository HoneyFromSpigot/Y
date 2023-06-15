package io.github.thewebcode.yplugin.scoreboard.scrolling;


import com.google.common.collect.Lists;
import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.utilities.TextCycler;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ScrollingScoreboardInformation {
    private static final YPlugin yPlugin = YPlugin.getInstance();

    private String prefix = "";

    private String suffix = "";

    private String titleText;
    private int len = 32;
    private TextCycler title;
    private List<ScrollingScoreboardEntry> entries;

    public ScrollingScoreboardInformation() {
        entries = new ArrayList<>();
    }

    public ScrollingScoreboardInformation title(String title) {
        titleText = title;
        this.title = new TextCycler(prefix, titleText, len);
        return this;
    }

    public ScrollingScoreboardInformation prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ScrollingScoreboardInformation(String title, ScrollingScoreboardEntry... entries) {
        this(null, title, entries);
    }

    public ScrollingScoreboardInformation(String prefix, String title, ScrollingScoreboardEntry... entries) {
        this(prefix, title, Lists.newArrayList(entries));
    }

    public ScrollingScoreboardInformation(String title, List<ScrollingScoreboardEntry> entries) {
        this(null, title, entries);
    }

    public ScrollingScoreboardInformation(String prefix, String title, List<ScrollingScoreboardEntry> entries) {
        Validate.notNull(title, "The scoreboard title must not be null.");
        Validate.noNullElements(entries, "Null scoreboard entries are not allowed. Consider using the Spacers class.");
        this.entries = Lists.newArrayList(entries);
        titleText = title;
        this.prefix = prefix;
    }

    public void setCyclerLength(int len) {
        if (title != null) {
            throw new IllegalStateException("The TextCycler instance has already been initialized, and its properties can no longer change.");
        }
        if (len <= 0) {
            throw new IllegalArgumentException("The length must be greater than zero.");
        } else if (len > 32) {
            throw new IllegalArgumentException("The length must not be greater than 32.");
        }
        this.len = len;
    }

    public TextCycler getTitle() {
        if (title == null) {
            title = new TextCycler(prefix, titleText, len);
        }
        return title;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setTitle(String title) {
        this.title = new TextCycler(prefix, title, len);
    }

    public void setEntries(ScrollingScoreboardEntry... entries) {
        this.entries.clear();
        Collections.addAll(this.entries, entries);
    }

    public void setEntries(List<ScrollingScoreboardEntry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);

    }

    public List<ScrollingScoreboardEntry> getEntries() {
        return entries;
    }

    @Override
    public int hashCode() {
        final int prime = 997;
        int result = 31;
        result = prime * result
                + ((entries == null) ? 0 : entries.hashCode());
        result = prime * result
                + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ScrollingScoreboardInformation)) {
            return false;
        }
        ScrollingScoreboardInformation other = (ScrollingScoreboardInformation) obj;
        if (entries == null) {
            if (other.entries != null) {
                return false;
            }
        } else if (!entries.equals(other.entries)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ScoreboardInformation [title=" + getTitle()
                + ", entries=" + getEntries() + "]";
    }
}
