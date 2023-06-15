package io.github.thewebcode.yplugin.sound;

import io.github.thewebcode.yplugin.location.Locations;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Set;

public class Sounds {
    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }

    public static void playSound(Player player, SoundData data) {
        playSound(player, data.getSound(), data.getVolume(), data.getPitch());
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void playSoundDistant(Player player, Location location, Sound sound) {
        player.playSound(location, sound, 1.0f, 1.0f);
    }

    public static void playSoundDistant(Player player, Location loc, SoundData data) {
        player.playSound(loc, data.getSound(), data.getVolume(), data.getPitch());
    }

    public static void playSoundDistant(Player player, Location location, Sound sound, float volume, float pitch) {
        player.playSound(location, sound, volume, pitch);
    }

    public static void playSoundForPlayerAtLocation(Player player, Location location, Sound sound, float volume, float pitch) {
        player.playSound(location, sound, volume, pitch);
    }

    public static void playSoundForPlayersAtLocation(Location location, Sound sound, float volume, float pitch) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    public static void playSoundForPlayersAtLocation(Location loc, SoundData sound) {
        playSoundForPlayersAtLocation(loc, sound.getSound(), sound.getVolume(), sound.getPitch());
    }

    public static void playSoundDistantAtLocation(Location areaLocation, Location soundPlayLocation, double radius, Sound sound, float volume, float pitch) {
        Set<Player> playersInLocation = Locations.getPlayersInRadius(areaLocation, radius);
        if (playersInLocation.size() == 0) {
            return;
        }

        for (Player player : playersInLocation) {
            playSoundDistant(player, soundPlayLocation, sound, volume, pitch);
        }
    }

    public static void playSoundDistantAtLocation(Location areaLocation, Location soundLocation, double radius, SoundData data) {
        playSoundDistantAtLocation(areaLocation, soundLocation, radius, data.getSound(), data.getVolume(), data.getPitch());
    }
}
