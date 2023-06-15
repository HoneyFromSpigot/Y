package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.inventory.ArmorBuilder;
import io.github.thewebcode.yplugin.inventory.ArmorInventory;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DebugArmorBuilder implements DebugAction {
    @Override
    public void doAction(Player player, String... args) {
        ArmorInventory tieredArmor = new ArmorBuilder()
                .withHelmet(Items.makeItem(Material.DIAMOND_HELMET))
                .withBoots(Items.makeItem(Material.IRON_BOOTS))
                .withChest(Items.makeItem(Material.LEATHER_CHESTPLATE))
                .withMainHand(Items.makeItem(Material.WOODEN_SWORD))
                .withOffHand(Items.makeItem(Material.SHIELD))
                .withLeggings(Items.makeItem(Material.LEATHER_LEGGINGS))
                .toInventory();

        Players.setArmor(player, tieredArmor);

        ItemStack[] armor = player.getInventory().getArmorContents();

        for (int i = 0; i < armor.length; i++) {
            Chat.message(player, String.format("&7[%s]&r: &7%s", i, Items.getName(armor[i])));
        }
    }

    @Override
    public String getActionName() {
        return "armor_builder";
    }
}
