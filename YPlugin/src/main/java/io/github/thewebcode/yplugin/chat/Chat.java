package io.github.thewebcode.yplugin.chat;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.menu.ChatMenu;
import io.github.thewebcode.yplugin.nms.NMS;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.sound.Sounds;
import io.github.thewebcode.yplugin.threading.RunnableManager;
import io.github.thewebcode.yplugin.time.Cooldown;
import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;
import io.github.thewebcode.yplugin.utilities.ArrayUtils;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Chat {
    private static YPlugin yPlugin = YPlugin.getInstance();

    private static ConsoleCommandSender commandSender = Bukkit.getConsoleSender();

    private static final Map<String, Cooldown> messageCooldowns = new HashMap<>();

    public static void broadcast(String... messages) {
        for (String message : messages) {
            Bukkit.broadcastMessage(StringUtil.formatColorCodes(message));
        }
    }

    public static void broadcastActionMessage(String message) {
        Players.stream().forEach(p -> actionMessage(p, message));
    }

    public static void messageConsole(String... messages) {
        String[] msgs = messages;
        for (int i = 0; i < msgs.length; i++) {
            msgs[i] = StringUtil.colorize(msgs[i]);
        }

        commandSender.sendMessage(msgs);
    }

    public static void messageConsole(Collection<String> messages) {
        for (String message : messages) {
            commandSender.sendMessage(StringUtil.formatColorCodes(message));
        }
    }

    public static void actionMessage(Player player, String message) {
        NMS.getActionMessageHandler().actionMessage(player, message);
    }

    public static void message(CommandSender target, String... messages) {
        sendMessage(target, messages);
    }


    public static void messageAll(String message) {
        for (Player player : Players.allPlayers()) {
            if (player == null) {
                continue;
            }
            sendMessage(player, message);
        }
    }

    public static void messageAll(Collection<Player> receivers, String... messages) {
        for (Player player : receivers) {
            if (player == null) {
                continue;
            }
            sendMessage(player, messages);
        }
    }

    public static void messageAllWithPermission(String permission, String... messages) {
        for (Player player : Players.allPlayers()) {
            if (player.hasPermission(permission)) {
                sendMessage(player, messages);
            }
        }
    }

    public static void messageAllWithoutPermission(String permission, String message) {
        for (Player player : Players.allPlayers()) {
            if (player.hasPermission(permission)) {
                sendMessage(player, message);
            }
        }
    }

    public static void messageAllWithoutPermission(String permission, String... messages) {
        for (Player player : Players.allPlayers()) {
            if (!player.hasPermission(permission)) {
                for (String message : messages) {
                    sendMessage(player, message);
                }
            }
        }
    }

    public static void sendMessage(CommandSender messageReceiver, String... messages) {
        for (String message : messages) {
            sendMessage(messageReceiver, message);
        }
    }

    public static void sendSoundedMessage(Player receiver, Sound sound, int delay, String... messages) {
        int index = 1;
        RunnableManager threadManager = YPlugin.getInstance().getThreadManager();
        for (String message : messages) {
            threadManager.runTaskLater(new DelayedMessage(receiver, message, sound), TimeHandler.getTimeInTicks(index * delay, TimeType.SECOND));
            index += 1;
        }
    }


    public static void messageOps(String... messages) {
        messageAll(Players.onlineOperators(), messages);
    }

    public static void messageAll(String... messages) {
        for (Player player : Players.allPlayers()) {
            sendMessage(player, messages);
        }
    }

    public static void messageAllExcept(String message, Player... exceptions) {
        UUID[] playerIds = ArrayUtils.getIdArray(exceptions);
        messageAll(Players.allPlayersExcept(playerIds), message);
    }

    public static void sendMessageOnCooldown(Player p, int cooldown, String message) {
        if (!messageCooldowns.containsKey(message)) {
            messageCooldowns.put(message, new Cooldown(cooldown));
        }

        Cooldown cool = messageCooldowns.get(message);

        if (cool.isOnCooldown(p)) {
            return;
        }

        cool.setOnCooldown(p);
        sendMessage(p, message);
    }

    public static void sendDelayedMessage(Player receiver, int delay, final String... messages) {
        int index = 1;
        RunnableManager threadManager = YPlugin.getInstance().getThreadManager();
        for (String message : messages) {
            threadManager.runTaskLater(new DelayedMessage(receiver, message), TimeHandler.getTimeInTicks(index * delay, TimeType.SECOND));
            index += 1;
        }
    }

    private static class DelayedMessage implements Runnable {

        private String message;
        private Sound sound = null;
        private UUID receiverId;

        public DelayedMessage(Player player, String message) {
            this.receiverId = player.getUniqueId();
            this.message = StringUtil.colorize(message);
        }

        public DelayedMessage(Player player, String message, Sound sound) {
            this(player, message);
            this.sound = sound;
        }

        @Override
        public void run() {
            Player player = Players.getPlayer(receiverId);

            message(player, message);
            if (sound != null) {
                Sounds.playSound(player, sound);
            }
        }
    }

    public static void sendRepeatedMessage(CommandSender messageReceiver, String message, int messageAmount) {
        for (int i = 0; i < messageAmount; i++) {
            sendMessage(messageReceiver, message);
        }
    }

    public static void sendMessage(CommandSender messageReceiver, String message) {
        if (messageReceiver == null || message == null) {
            return;
        }
        messageReceiver.sendMessage(StringUtil.formatColorCodes(message));
    }

    public static void debug(String... message) {
        yPlugin.debug(message);
    }

    @Deprecated
    public static void formatDebug(String msg, Object... formatting) {
        yPlugin.debug(String.format(msg, formatting));
    }

    public static String format(String text) {
        return StringUtil.formatColorCodes(text);
    }

    public static String format(String text, Object... args) {
        return String.format(StringUtil.formatColorCodes(text), args);
    }

    public static void format(Player p, String text, Object... args) {
        message(p, format(text, args));
    }

    public static ChatMenu createMenu() {
        return new ChatMenu();
    }

}