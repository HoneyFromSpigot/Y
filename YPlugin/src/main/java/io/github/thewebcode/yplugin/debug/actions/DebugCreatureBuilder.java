package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.entity.CreatureBuilder;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.time.TimeHandler;
import io.github.thewebcode.yplugin.time.TimeType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DebugCreatureBuilder implements DebugAction {

    @Override
    public void doAction(Player player, String... args) {
        LivingEntity zombie = CreatureBuilder.of(EntityType.ZOMBIE)
                .armor()
                .withHelmet(Items.makeItem(Material.IRON_HELMET))
                .withBoots(Items.makeItem(Material.GOLDEN_BOOTS))
                .withMainHand(Items.makeItem(Material.WOODEN_SWORD))
                .parent()
                .spawn(player.getLocation());

        LivingEntity skeleton = CreatureBuilder.of(EntityType.SKELETON)
                .armor()
                .withMainHand(Items.makeItem(Material.DIAMOND_SWORD))
                .parent()
                .spawn(player.getLocation());

        Chat.message(player,
                "&eSpawned an adult zombie with an iron helmet, gold boots, and a wooden sword",
                "Spawned a wither skeleton with a diamond sword"
        );

        YPlugin.getInstance().getThreadManager().runTaskLater(new BukkitRunnable() {
            @Override
            public void run() {
                skeleton.remove();
                zombie.remove();
            }
        }, TimeHandler.getTimeInTicks(3, TimeType.SECOND));
    }

    @Override
    public String getActionName() {
        return "creature_builder";
    }
}
