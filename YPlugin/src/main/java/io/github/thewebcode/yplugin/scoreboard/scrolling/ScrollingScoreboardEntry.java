package io.github.thewebcode.yplugin.scoreboard.scrolling;

import io.github.thewebcode.yplugin.utilities.TextCycler;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

public final class ScrollingScoreboardEntry {
    private String value;
    private String prefix;

    private TextCycler cycler;

    public ScrollingScoreboardEntry(String value) {
        this((String) null, value);
    }

    public ScrollingScoreboardEntry(ChatColor prefix, String value) {
        this(new ChatColor[]{prefix}, value);
    }

    public ScrollingScoreboardEntry(ChatColor[] prefix, String value) {
        Validate.notNull(value, "A text value must be specified.");
        StringBuilder prefixBuilder = new StringBuilder(prefix == null ? 0 : prefix.length * 2);
        if (prefix != null) {
            for (int i = 0; i < prefix.length; i++) {
                Validate.notNull(prefix[i], "Null prefixes are not allowed.");
                prefixBuilder.append(prefix[i]);
            }
        }
        this.value = value;
        this.prefix = prefixBuilder.toString();
    }

    public ScrollingScoreboardEntry(String prefix, String value) {
        Validate.notNull(value, "A text value must be specified.");
        this.value = value;
        this.prefix = prefix == null ? "" : prefix;
    }

    public void setValue(String text) {
        this.value = text;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getValue() {
        return value;
    }

    public String getPrefix() {
        return prefix;
    }

    public TextCycler createCycler() {
        if (cycler == null || !cycler.getText().equals(getValue()) || (cycler.getPrefix() != null && !cycler.getPrefix().equals(getPrefix()))) {
            cycler = new TextCycler(getPrefix(), getValue(), 16);
        }
        return cycler;
    }

    @Override
    public String toString() {
        return getPrefix() + getValue();
    }

    @Override
    public int hashCode() {
        final int prime = 3119;
        int result = 83;
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        if (!(obj instanceof ScrollingScoreboardEntry)) {
            return false;
        }
        ScrollingScoreboardEntry other = (ScrollingScoreboardEntry) obj;
        if (prefix == null) {
            if (other.prefix != null) {
                return false;
            }
        } else if (!prefix.equals(other.prefix)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
