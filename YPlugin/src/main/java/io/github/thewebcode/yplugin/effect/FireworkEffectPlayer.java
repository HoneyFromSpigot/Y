package io.github.thewebcode.yplugin.effect;

import io.github.thewebcode.yplugin.reflection.ReflectionUtilities;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.Method;

public class FireworkEffectPlayer {

    private Method world_getHandle = null;
    private Method nms_world_broadcastEntityEffect = null;
    private Method firework_getHandle = null;

    public void playFirework(Location loc, FireworkEffect fe) throws Exception {
        playFirework(loc.getWorld(), loc, fe);
    }

    public void playFirework(World world, Location loc, FireworkEffect fe) throws Exception {
        Firework fw = world.spawn(loc, Firework.class);
        Object nms_world = null;
        Object nms_firework = null;
        if (world_getHandle == null) {
            world_getHandle = ReflectionUtilities.getMethod(world.getClass(), "getHandle");
            firework_getHandle = ReflectionUtilities.getMethod(fw.getClass(), "getHandle");
        }
        nms_world = world_getHandle.invoke(world, (Object[]) null);
        nms_firework = firework_getHandle.invoke(fw, (Object[]) null);
        if (nms_world_broadcastEntityEffect == null) {
            nms_world_broadcastEntityEffect = ReflectionUtilities.getMethod(nms_world.getClass(), "broadcastEntityEffect");
        }
        FireworkMeta data = fw.getFireworkMeta();
        data.clearEffects();
        data.setPower(1);
        data.addEffect(fe);
        fw.setFireworkMeta(data);
        nms_world_broadcastEntityEffect.invoke(nms_world, new Object[]{nms_firework, (byte) 17});
        fw.remove();
    }

}