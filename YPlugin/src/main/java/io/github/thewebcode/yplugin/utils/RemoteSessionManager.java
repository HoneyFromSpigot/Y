package io.github.thewebcode.yplugin.utils;

import io.github.thewebcode.yplugin.YPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoteSessionManager {
    private HashMap<String, String> serverKeyList;

    public RemoteSessionManager(){
        this.serverKeyList = new HashMap<>();
    }

    public void addKey(String playerName, String key){
        LoggingService.info("Adding key for " + playerName);
        serverKeyList.put(playerName, key);

        new BukkitRunnable(){
            @Override
            public void run() {
                if(serverKeyList.containsKey(playerName)){
                    YPlugin.getInstance().getRemoteSessionManager().remove(playerName);
                }
            }
        }.runTaskLater(YPlugin.getInstance(), 20 * 60 * 2);
    }

    public void remove(String playerName){
        if(serverKeyList.containsKey(playerName)){
            LoggingService.info("Removing key for " + playerName);
            serverKeyList.remove(playerName);
        }
    }

    public boolean hasValidKey(String playerName){
        if(serverKeyList.containsKey(playerName)){
            return isKeyValid(playerName, serverKeyList.get(playerName));
        }

        return false;
    }

    public boolean isKeyValid(String playerName, String key){
        return serverKeyList.containsValue(key);
    }
}
