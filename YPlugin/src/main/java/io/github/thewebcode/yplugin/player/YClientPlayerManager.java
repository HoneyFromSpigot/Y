package io.github.thewebcode.yplugin.player;

import io.github.thewebcode.yplugin.nms.NMS;
import io.github.thewebcode.yplugin.nms.NmsPlayer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class YClientPlayerManager {
    private ArrayList<Player> yClientPlayers = new ArrayList<>();

    public void addPlayer(Player player){
        yClientPlayers.add(player);
    }

    public void removePlayer(Player player){
        if(yClientPlayers.contains(player)){
            yClientPlayers.remove(player);
        }
    }

    public boolean isYClientPlayer(Player player){
        return yClientPlayers.contains(player);
    }

    public ArrayList<Player> getYClientPlayers(){
        return yClientPlayers;
    }

    public void flush(){
        ArrayList<Player> temp = new ArrayList<>(yClientPlayers);
        yClientPlayers.clear();

        Bukkit.getOnlinePlayers().forEach(player -> {
            NmsPlayer nmsPlayer = NMS.getNMSPlayer(player);
            nmsPlayer.sendPacket(new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "resend_hello"), new PacketDataSerializer(Unpooled.buffer())));
        });
    }
}
