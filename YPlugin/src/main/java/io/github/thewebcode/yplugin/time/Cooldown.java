package io.github.thewebcode.yplugin.time;

import io.github.thewebcode.yplugin.chat.Chat;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Cooldown {
    private HashMap<UUID, Long> cooldowns = new HashMap<>();
    private long cooldownTime = 0;

    public Cooldown(int seconds) {
        this.cooldownTime = seconds * 1000;
    }

    public void removeCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return;
        }

        cooldowns.remove(player.getUniqueId());
    }

    public void setOnCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), Long.sum(System.currentTimeMillis(), cooldownTime));
    }

    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime * 1000;
    }

    public double getRemainingSeconds(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return 0;
        }

        long playerOffCooldown = cooldowns.get(player.getUniqueId());
        long currentTime = System.currentTimeMillis();
        long difference = playerOffCooldown - currentTime;

        Chat.debug("Difference in time from then to now on " + player.getName() + " is " + difference + " seconds");
        if (difference <= 0) {
            return 0;
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
        return (double) seconds;
    }

    public double getRemainingMinutes(Player player) {
        return getRemainingSeconds(player) / 60;
    }

    public boolean isOnCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }

        double seconds = getRemainingSeconds(player);

        return seconds > 0;
    }
}
