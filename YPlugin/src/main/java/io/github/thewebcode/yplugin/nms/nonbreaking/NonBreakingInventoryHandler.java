package io.github.thewebcode.yplugin.nms.nonbreaking;

import io.github.thewebcode.yplugin.nms.InventoryHandler;
import io.github.thewebcode.yplugin.reflection.ReflectionUtilities;
import org.bukkit.inventory.Inventory;

public class NonBreakingInventoryHandler implements InventoryHandler {
    static Class<?> obcCraftInventory;
    static Class<?> obcCraftInventoryCustom;
    static Class<?> obcMinecraftInventory;

    static {
        try {
            obcCraftInventory = ReflectionUtilities.getOBCClass("inventory.CraftInventory");
            obcCraftInventoryCustom = ReflectionUtilities.getOBCClass("inventory.CraftInventoryCustom");
            for (Class<?> c : obcCraftInventoryCustom.getDeclaredClasses()) {
                if (c.getSimpleName().equals("MinecraftInventory")) {
                    obcMinecraftInventory = c;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeTitle(Inventory inv, String title) {
        try {
            Object minecrafInventory = ReflectionUtilities.setAccessible(obcCraftInventory.getDeclaredField("inventory")).get(inv);
            ReflectionUtilities.setAccessible(obcMinecraftInventory.getDeclaredField("title")).set(minecrafInventory, title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
