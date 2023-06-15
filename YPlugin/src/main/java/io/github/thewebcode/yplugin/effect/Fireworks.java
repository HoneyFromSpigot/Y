package io.github.thewebcode.yplugin.effect;

import io.github.thewebcode.yplugin.entity.Entities;
import io.github.thewebcode.yplugin.utilities.ArrayUtils;
import io.github.thewebcode.yplugin.utilities.NumberUtil;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class Fireworks {
    private static final Random random = new Random();
    private static final Type[] FIREWORK_TYPES = Type.values();
    public static final Color[] FIREWORK_COLOURS = new Color[]{
            Color.WHITE,
            Color.AQUA,
            Color.BLACK,
            Color.BLUE,
            Color.FUCHSIA,
            Color.GRAY,
            Color.GREEN,
            Color.MAROON,
            Color.NAVY,
            Color.OLIVE,
            Color.RED,
            Color.LIME,
            Color.YELLOW,
            Color.ORANGE,
            Color.TEAL,
            Color.PURPLE,
            Color.SILVER
    };

    private static FireworkEffectPlayer fplayer = new FireworkEffectPlayer();

    public static Type randomType() {
        return ArrayUtils.getRandom(FIREWORK_TYPES);
    }

    public static Color randomFireworkColor() {
        return ArrayUtils.getRandom(FIREWORK_COLOURS);
    }

    public static Color randomColor() {
        return Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public static Color[] randomColors(int amount) {
        Color[] colors = new Color[amount];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = randomColor();
        }
        return colors;
    }

    public static Color[] randomFireworkColors(int amount) {
        Color[] colors = new Color[amount];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = randomFireworkColor();
        }
        return colors;
    }

    private static boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static void playFirework(Location location, FireworkEffect fwEffect) {
        try {
            fplayer.playFirework(location, fwEffect);
        } catch (Exception e) {
            Firework firework = Entities.spawnFireworks(location);
            FireworkMeta meta = firework.getFireworkMeta();

            meta.addEffects(fwEffect);
            meta.setPower(NumberUtil.getRandomInRange(1, 4));

            firework.setFireworkMeta(meta);
        }
    }

    public static void playRandomFirework(Location location) {
        playFirework(location, randomFireworkEffect());
    }

    public static void playRandomFireworks(Location location, int count) {
        for (int i = 0; i < count; i++) {
            playRandomFirework(location);
        }
    }

    public static FireworkEffect randomFireworkEffect() {
        return FireworkEffect.builder().with(randomType()).withColor(randomFireworkColors(NumberUtil.getRandomInRange(2, 6))).withFade(randomColor()).trail(randomBoolean()).flicker(randomBoolean()).build();
    }
}