package io.github.thewebcode.yplugin.nms;

import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class AbstractTitle {
    private String title = null;
    private ChatColor titleColor = ChatColor.WHITE;
    private String subtitle = null;
    private ChatColor subtitleColor = ChatColor.WHITE;
    private int fadeInTime = -1;
    private int stayTime = -1;
    private int fadeOutTime = -1;
    private boolean ticks = false;

    public AbstractTitle(String title) {
        this.title = title;
    }

    public AbstractTitle(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public AbstractTitle(AbstractTitle title) {
        this.title = title.title;
        this.subtitle = title.subtitle;
        this.titleColor = title.titleColor;
        this.subtitleColor = title.subtitleColor;
        this.fadeInTime = title.fadeInTime;
        this.fadeOutTime = title.fadeOutTime;
        this.stayTime = title.stayTime;
        this.ticks = title.ticks;
    }

    public AbstractTitle(String title, String subtitle, int fadeInTime, int stayTime,
                         int fadeOutTime) {
        this.title = StringUtil.colorize(title);
        this.subtitle = StringUtil.colorize(subtitle);
        this.fadeInTime = fadeInTime;
        this.stayTime = stayTime;
        this.fadeOutTime = fadeOutTime;
    }

    public void setTitle(String title) {
        this.title = StringUtil.colorize(title);
    }

    public String getTitle() {
        return this.title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = StringUtil.colorize(subtitle);
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setTitleColor(ChatColor color) {
        this.titleColor = color;
    }

    public void setSubtitleColor(ChatColor color) {
        this.subtitleColor = color;
    }

    public void setFadeInTime(int time) {
        this.fadeInTime = time;
    }

    public void setFadeOutTime(int time) {
        this.fadeOutTime = time;
    }

    public void setStayTime(int time) {
        this.stayTime = time;
    }

    public void setTimingsToTicks() {
        ticks = true;
    }

    public void setTimingsToSeconds() {
        ticks = false;
    }

    public ChatColor getTitleColor() {
        return titleColor;
    }

    public ChatColor getSubtitleColor() {
        return subtitleColor;
    }

    public int getFadeInTime() {
        if (ticks) {
            return (int) TimeHandler.getTimeInTicks(fadeInTime, TimeType.SECOND);
        }
        return fadeInTime;
    }

    public int getStayTime() {
        if (ticks) {
            return (int) TimeHandler.getTimeInTicks(stayTime, TimeType.SECOND);
        }
        return stayTime;
    }

    public int getFadeOutTime() {
        if (ticks) {
            return (int) TimeHandler.getTimeInTicks(fadeOutTime, TimeType.SECOND);
        }
        return fadeOutTime;
    }

    public boolean isTicks() {
        return ticks;
    }

    public abstract void send(Player player);

    public void broadcast() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            send(p);
        }
    }

    public abstract void clearTitle(Player player);

    public abstract void resetTitle(Player player);

    public interface TitleHandler {
        void send(Player player, AbstractTitle title);

        void resetTitle(Player player);

        void clearTitle(Player player);
    }
}
