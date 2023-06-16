package io.github.thewebcode.yplugin.nms;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;

public interface NmsPlayer {
    void sendPacket(Packet<?> packet);

    PlayerConnection getPlayerConnection();
    EntityPlayer getHandle();
}
