package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.FlagArg;
import io.github.thewebcode.yplugin.command.Flags;
import io.github.thewebcode.yplugin.entity.CreatureBuilder;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.sound.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnMobCommand {
    @Command(identifier = "spawnmob", permissions = Perms.COMMAND_MOB_SPAWN)
    @Flags(identifier = {"baby", "age", "size", "villager", "powered", "wither", "helmet", "chest", "legs", "boots", "mainhand", "offhand"})
    public void onSpawnMobCommand(Player player, @Arg(name = "mob type") EntityType type, @Arg(name = "amount", def = "1") int amount,
                                  @FlagArg("baby") boolean baby,
                                  @FlagArg("age") @Arg(name = "age", def = "0") int age,
                                  @FlagArg("size") @Arg(name = "size", def = "0") int size,
                                  @FlagArg("powered") boolean powered,
                                  @FlagArg("helmet") @Arg(name = "helmet", def = "0") ItemStack helmet,
                                  @FlagArg("chest") @Arg(name = "chest", def = "0") ItemStack chest,
                                  @FlagArg("legs") @Arg(name = "legs", def = "0") ItemStack legs,
                                  @FlagArg("boots") @Arg(name = "boots", def = "0") ItemStack boots,
                                  @FlagArg("mainhand") @Arg(name = "mainhand", def = "0") ItemStack mainHand,
                                  @FlagArg("offhand") @Arg(name = "offhand", def = "0") ItemStack offHand) {
        if (type == null) {
            return;
        }

        if (type == EntityType.ENDER_DRAGON && !Players.hasPermission(player, Perms.COMMAND_MOB_SPAWN_ENDERDRAGON)) {
            Chat.message(player, Messages.permissionRequired(Perms.COMMAND_MOB_SPAWN_ENDERDRAGON));
            return;
        }

        CreatureBuilder spawner = CreatureBuilder.of(type)
                .asBaby(baby)
                .age(age)
                .size(size);

        if (powered) {
            spawner.powered();
        }

        if (Items.isArmor(helmet)) {
            spawner.armor().withHelmet(helmet);
        }

        if (Items.isArmor(chest)) {
            spawner.armor().withChest(chest);
        }

        if (Items.isArmor(legs)) {
            spawner.armor().withLeggings(legs);
        }

        if (Items.isArmor(boots)) {
            spawner.armor().withBoots(boots);
        }

        if (mainHand != null && mainHand.getType() != Material.AIR) {
            spawner.armor().withMainHand(mainHand);
        }

        if (offHand != null && offHand.getType() != Material.AIR) {
            spawner.armor().withOffHand(offHand);
        }

        spawner.spawn(Players.getTargetLocation(player), amount);
        Sounds.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        Chat.actionMessage(player, "&aYour creature has come to life!");
    }
}
