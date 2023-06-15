package io.github.thewebcode.yplugin.command.handlers;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.command.ArgumentHandler;
import io.github.thewebcode.yplugin.command.CommandArgument;
import io.github.thewebcode.yplugin.command.TransformError;
import io.github.thewebcode.yplugin.item.Enchantments;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentArgumentHandler extends ArgumentHandler<Enchantment> {
    public EnchantmentArgumentHandler() {
    }

    @Override
    public Enchantment transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        if (!Enchantments.isEnchantment(value)) {
            throw new TransformError(Messages.invalidEnchantment(value));
        }

        return Enchantments.getEnchantment(value);
    }
}
