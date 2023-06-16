package io.github.thewebcode.yplugin.networking;

import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

public class Packet {
    private Player player;
    private ByteBuf buf;
    private String name;

    public Packet (ByteBuf buf, String name, Player player) {
        this.buf = buf;
        this.name = name;
        this.player = player;
    }

    public String bufAsString(){
        int length = buf.readableBytes();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes);
    }

    public ByteBuf getBuf() {
        return buf;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public static String readBufAsString(ByteBuf buf){
        int length = buf.readableBytes();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes);
    }
}
