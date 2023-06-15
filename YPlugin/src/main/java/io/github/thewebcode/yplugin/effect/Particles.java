package io.github.thewebcode.yplugin.effect;

import io.github.thewebcode.yplugin.location.Locations;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Random;

public class Particles {
    private static Random rand = new Random();

    public static void sendToPlayer(Player player, Particle effect, Location loc, int count) {
        player.spawnParticle(effect,loc,count);
    }

    public static void sendToLocation(Location loc, Particle effect, float offsetX, float offsetY, float offsetZ, float speed, int count) {
        sendToLocation(loc, effect, offsetX, offsetY, offsetZ, speed, count, 30);
    }

    public static void sendToLocation(Location loc, Particle effect, float offsetX, float offsetY, float offsetZ, float speed, int count, int radius) {
        for (Player player : Locations.getPlayersInRadius(loc, 30)) {
            sendToPlayer(player, effect, loc, count);
        }
    }


    public static void sendToLocation(Location loc, Particle effect, int count, int radius) {
        sendToLocation(loc, effect, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), count, radius);
    }

    public static void sendToLocation(Location loc, Particle effect, int count) {
        sendToLocation(loc, effect, count, ParticleEffect.PARTICLE_RADIUS);
    }

}
