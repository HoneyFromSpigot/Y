package io.github.thewebcode.yplugin.nms.minecraft_1_20_R1;

import io.github.thewebcode.yplugin.nms.NmsPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NmsPlayer_1_20_R1 implements NmsPlayer {
    private Player player;

    public NmsPlayer_1_20_R1(Player player){
        this.player = player;
    }
    @Override
    public void sendPacket(Packet<?> packet) {
        getPlayerConnection().a(packet);
    }

    @Override
    public PlayerConnection getPlayerConnection() {
        return getHandle().c;
    }

    @Override
    public EntityPlayer getHandle() {
        return ((CraftPlayer) player).getHandle();
    }

    public Player getPlayer() {
        return player;
    }
}
