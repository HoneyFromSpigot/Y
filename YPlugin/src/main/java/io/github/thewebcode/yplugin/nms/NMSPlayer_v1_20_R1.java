package io.github.thewebcode.yplugin.nms;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSPlayer_v1_20_R1 implements NMSPlayer{
    private CraftPlayer player;

    public NMSPlayer_v1_20_R1(Player player){
        this.player = (CraftPlayer) player;
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        getConnection().a(packet);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public PlayerConnection getConnection() {
        return getHandle().c;
    }

    @Override
    public EntityPlayer getHandle() {
        return player.getHandle();
    }
}
