package io.github.thewebcode.yplugin.command;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.utils.LanguageService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.nio.ByteBuffer;

public class YLoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,Command command,String s,String[] strings) {
        if(!(sender instanceof Player)){
            sender.sendMessage(LanguageService.get(LanguageService.Language.DEFAULT, LanguageService.MessageKey.COMMAND_PLAYER_ONLY));
            return false;
        }

        Player player = (Player) sender;
        PacketPlayOutCustomPayload payload = new PacketPlayOutCustomPayload(new MinecraftKey("yplugin", "start_login"), new PacketDataSerializer(Unpooled.buffer()));
        ((CraftPlayer) player).getHandle().b.a(payload);

        return false;
    }
}
