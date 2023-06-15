package io.github.thewebcode.yplugin.game.gadget;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.game.guns.BaseGun;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.plugin.Plugins;
import io.github.thewebcode.yplugin.world.Worlds;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Gadgets {
    private static final AtomicInteger ids = new AtomicInteger(0);

    private static final Random random = new Random();

    private static final Map<Integer, Gadget> gadgets = new LinkedHashMap<>();

    public static void registerGadget(Gadget gadget) {
        gadgets.put(gadget.id(), gadget);
        Plugins.registerListener(YPlugin.getInstance(), gadget);
    }

    public static boolean isGadget(ItemStack item) {
        return getGadget(item) != null;
    }

    public static boolean isGadget(int id) {
        return gadgets.containsKey(id);
    }

    public static Gadget getGadget(ItemStack item) {
        if (item == null) {
            return null;
        }

        for (Gadget gadget : gadgets.values()) {
            if (gadget instanceof BaseGun) {

                BaseGun gun = (BaseGun) gadget;
                if (Items.nameContains(item, gun.getItemName())) {
                    return gun;
                }
            } else if (gadget.getItem().isSimilar(item)) {
                return gadget;
            }
        }
        return null;
    }

    public static Gadget getGadget(int id) {
        return gadgets.get(id);
    }

    public static void spawnGadget(Gadget gadget, Location location) {
        Worlds.dropItem(location, gadget.getItem());
    }

    public static Gadget getRandomGadget() {
        List<Gadget> gadgetList = Lists.newArrayList(gadgets.values());
        return gadgetList.get(random.nextInt(gadgetList.size()));
    }

    public static Collection<Gadget> getAllGadgets() {
        return gadgets.values();
    }

    public static int getGadgetCount() {
        return gadgets.size();
    }


    public static int getFirstFreeId() {
        int id = 0;

        do {
            id = ids.getAndIncrement();
        } while (gadgets.containsKey(id));

        if (id >= Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Unable to obtain ID for gadget; No identifiers available.");
        }

        return id;
    }

    public static boolean hasBeenRegistered(Gadget gadget) {
        return gadgets.containsKey(gadget.id());
    }

    public static int getId(ItemStack item) {
        if (!isGadget(item)) {
            return -1;
        }

        for (Gadget gadget : gadgets.values()) {
            if (gadget instanceof BaseGun) {

                BaseGun gun = (BaseGun) gadget;
                if (Items.nameContains(item, gun.getItemName())) {
                    return gun.id();
                }
            } else if (gadget.getItem().isSimilar(item)) {
                return gadget.id();
            }
        }

        return -1;
    }
}
