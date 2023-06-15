package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.item.SkullCreator;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.sound.Sounds;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.codehaus.plexus.util.Base64;

public class SkullCommand {
    @Command(identifier = "skull", permissions = Perms.COMMAND_SKULL)
    public void getPlayerSkullCommand(Player player, @Arg(name = "value") String value) {
        ItemStack newSkull = null;

        if (Base64.isArrayByteBase64(value.getBytes())) {
            newSkull = SkullCreator.itemFromBase64(value);
        } else {
            newSkull = Items.getSkull(value);
        }
        player.getInventory().addItem(newSkull);
        Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP);
    }
}
