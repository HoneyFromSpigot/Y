package io.github.thewebcode.yplugin.nms;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.effect.ParticleEffect;
import io.github.thewebcode.yplugin.location.Locations;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public interface ParticleEffectsHandler {
    Object createParticleEffectPacket(ParticleEffect effect, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, int count, int... extra);

    default Object createParticleEffectPacket(ParticleEffect effect, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int count, int... extra) {
        return createParticleEffectPacket(effect, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), offsetX, offsetY, offsetZ, speed, count, extra);
    }

    default Object createParticleEffectPacket(ParticleEffect effect, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int count) {
        return createParticleEffectPacket(effect, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), offsetX, offsetY, offsetZ, speed, count, 0);
    }

    default void sendToPlayer(Player player, ParticleEffect effect, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int count) {
        if (effect == ParticleEffect.NONE) {
            return;
        }

        try {
            Object packet = createParticleEffectPacket(effect, loc, offsetX, offsetY, offsetZ, speed, count);
            NmsPlayers.sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void sendToPlayer(Player player, ParticleEffect effect, Location loc, int count) {
        Random rand = new Random();
        sendToPlayer(player, effect, loc, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), count);
    }

    default void sendToLocation(Location loc, ParticleEffect effect, float offsetX, float offsetY, float offsetZ, float speed, int count) {
        sendToLocation(loc, effect, offsetX, offsetY, offsetZ, speed, count, ParticleEffect.PARTICLE_RADIUS);
    }

    default void sendToLocation(Location loc, ParticleEffect effect, float offsetX, float offsetY, float offsetZ, float speed, int count, int radius) {
        if (effect == ParticleEffect.NONE) {
            return;
        }

        try {
            Object packet = createParticleEffectPacket(effect, loc, offsetX, offsetY, offsetZ, speed, count, 0);
            for (Player player : Locations.getPlayersInRadius(loc, radius)) {
                MinecraftPlayer mcPlayer = YPlugin.getInstance().getPlayerHandler().getData(player);
                if (mcPlayer.isHidingOtherPlayers()) {
                    continue;
                }

                NmsPlayers.sendPacket(player, packet);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    default void sendToLocation(Location loc, ParticleEffect effect, int count, int radius) {
        Random rand = new Random();
        sendToLocation(loc, effect, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), count, radius);
    }

    default void sendToLocation(Location loc, ParticleEffect effect, int count) {
        sendToLocation(loc, effect, count, ParticleEffect.PARTICLE_RADIUS);
    }
}
