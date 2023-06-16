package io.github.thewebcode.yplugin.networking;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.PacketPlayInCustomPayload;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class MessageOutboundHandler extends ChannelOutboundHandlerAdapter {
    public static final String NAME = "io.github.thewebcode.y:outbound_handler";
    private final Player player;
    private final UUID playerUUID;

    private MessageOutboundHandler(Player player) {
        this.player = player;
        this.playerUUID = player.getUniqueId();

        attach(player);
    }

    public void detach(){
        try{
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            PlayerConnection connection = entityPlayer.c;

            Field field = connection.getClass().getField("h");
            NetworkManager networkManager = (NetworkManager) field.get(connection);
            ChannelPipeline pipeline = networkManager.m.pipeline();
            pipeline.remove(NAME);
        } catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        } catch (NoSuchElementException ignored) {}
    }


    private void attach(Player player) {
        try {
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            PlayerConnection connection = entityPlayer.c;

            Field field = connection.getClass().getField("h");
            NetworkManager networkManager = (NetworkManager) field.get(connection);
            ChannelPipeline pipeline = networkManager.m.pipeline();
            detach();
            pipeline.addAfter("decoder", NAME, new MessageToMessageDecoder<PacketPlayInCustomPayload>() {
                @Override
                protected void decode(ChannelHandlerContext channelHandlerContext, PacketPlayInCustomPayload packetPlayInCustomPayload, List<Object> list) throws Exception {
                    list.add(packetPlayInCustomPayload);
                    read(packetPlayInCustomPayload);
                }
            });

            System.out.println("Attached to " + player.getName());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void read(PacketPlayInCustomPayload payload){
        String packet = payload.c.toString();
        if(packet.contains("yfabric")) {
            String packetName = packet.replace("yfabric:", "");
            PacketDataUtil.handle(new Packet(payload.d, packetName, player));
        }
    }

    public static class Builder{
        private Player player;

        public Builder(Player player){
            this.player = player;
        }

        public MessageOutboundHandler build(){
            return new MessageOutboundHandler(player);
        }
    }
}
