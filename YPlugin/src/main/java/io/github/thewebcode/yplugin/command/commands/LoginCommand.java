package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.nms.NMS;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.entity.Player;

public class LoginCommand {
    @Command(identifier = "login", onlyPlayers = true)
    public void onLogin(Player p){
        PacketPlayOutCustomPayload payload = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "start_login"), new PacketDataSerializer(Unpooled.buffer()));
        NMS.getNMSPlayer(p).sendPacket(payload);
    }
}
