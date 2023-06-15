package io.github.thewebcode.yplugin.chat;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PrivateMessageManager {
    private Map<String, PrivateMessage> recentChatters = new HashMap<>();

    public PrivateMessageManager() {

    }

    public boolean hasRecentPrivateMessageFrom(String playerName) {
        return recentChatters.containsKey(playerName);
    }


    public void setRecentPrivateMessageFrom(String playerFor, PrivateMessage privateMessage) {
        recentChatters.put(playerFor, privateMessage);
    }

    public String getMostRecentPrivateMessager(String playerFor) {
        return recentChatters.get(playerFor).getPlayerSendingMessage();
    }


    public void messagePlayer(Player playerSendingTo, Player playerSendingFrom, String message) {
        Chat.sendMessage(playerSendingTo, "&f[&e" + playerSendingFrom.getDisplayName() + "&b -> &aYou&f] " + message);
        Chat.sendMessage(playerSendingFrom, "&f[&eYou &b-> &a" + playerSendingTo.getDisplayName() + "&f] " + message);
        setRecentPrivateMessageFrom(playerSendingTo.getName(), new PrivateMessage(playerSendingFrom.getName(), playerSendingTo.getName()));
    }
}
