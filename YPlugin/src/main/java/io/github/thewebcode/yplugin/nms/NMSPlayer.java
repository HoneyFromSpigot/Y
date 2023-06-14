package io.github.thewebcode.yplugin.nms;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.entity.Player;

public interface NMSPlayer {
    void sendPacket(Packet<?> packet);

    Player getPlayer();

    PlayerConnection getConnection();

    EntityPlayer getHandle();
}
