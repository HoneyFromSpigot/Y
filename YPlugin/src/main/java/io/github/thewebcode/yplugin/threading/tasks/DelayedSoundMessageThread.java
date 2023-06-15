package io.github.thewebcode.yplugin.threading.tasks;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.sound.Sounds;
import io.github.thewebcode.yplugin.time.Cooldown;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DelayedSoundMessageThread implements Runnable {

    private UUID playerId;

    private String[] messages;

    private int messageIndex = 0;

    private int finalIndex = 0;

    private Sound sound;

    private Cooldown messageCooldown;

    private int taskId = 0;

    public DelayedSoundMessageThread(Player player, Sound sound, int secondsDelay, String... messages) {
        playerId = player.getUniqueId();
        this.sound = sound;
        this.messages = messages;
        if (messages.length == 1) {
            messages = StringUtil.splitOnNewline(messages[0]);
        }


        finalIndex = messages.length - 1;
        messageCooldown = new Cooldown(secondsDelay);
    }

    public void setTaskId(int id) {
        taskId = id;
    }

    @Override
    public void run() {
        if (messageIndex >= finalIndex) {
            YPlugin.getInstance().getThreadManager().cancelTask(taskId);
            return;
        }

        Player player = Players.getPlayer(playerId);
        if (messageCooldown.isOnCooldown(player)) {
            return;
        }
        try {
            String message = messages[messageIndex];
            Chat.message(player, message);
            Sounds.playSound(player, sound);
            messageCooldown.setOnCooldown(player);
            messageIndex++;
        } catch (IndexOutOfBoundsException e) {
            YPlugin.getInstance().debug("Uh oh! Message index[" + messageIndex + "] doesn't exist!\n" + StringUtil.joinString(messages, "\n"));
        }
    }
}
