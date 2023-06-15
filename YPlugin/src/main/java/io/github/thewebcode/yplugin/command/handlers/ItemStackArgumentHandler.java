package io.github.thewebcode.yplugin.command.handlers;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.command.*;
import io.github.thewebcode.yplugin.exceptions.InvalidMaterialNameException;
import io.github.thewebcode.yplugin.inventory.HandSlot;
import io.github.thewebcode.yplugin.item.Items;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.utilities.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.EnumSet;

public class ItemStackArgumentHandler extends ArgumentHandler<ItemStack> {
    public ItemStackArgumentHandler() {
        addVariable("hand", "item in the hand of the command executor", ItemStackHandArgumentVariable.getInstance());
        addVariable("offhand", "item in the off-hand of the command executor", ItemStackHandArgumentVariable.getInstance());
        addVariable("sender", "item in the hand of the command executor", ItemStackHandArgumentVariable.getInstance());

        for (Material material : Material.values()) {
            try {
                addVariable(material.name().toLowerCase(), material.name().toLowerCase(), ItemStackArgumentVariable.getInstance());
            } catch (Exception ex) {
                Bukkit.getLogger().info(StringUtil.getStackStr(ex));
            }
        }
    }

    @Override
    public ItemStack transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        if (value.equalsIgnoreCase("0") || value.equalsIgnoreCase("air")) {
            return null;
        }

        try {
            return new ItemStack(Items.getMaterialByName(value));
        } catch (InvalidMaterialNameException e) {
            throw new TransformError(e.getMessage());
        }

    }

    private static class ItemStackArgumentVariable implements ArgumentVariable<ItemStack> {

        private static ItemStackArgumentVariable instance;

        public static ItemStackArgumentVariable getInstance() {
            if (instance == null) {
                instance = new ItemStackArgumentVariable();
            }
            return instance;
        }

        private ItemStackArgumentVariable() {
        }

        @Override
        public ItemStack var(CommandSender sender, CommandArgument argument, String varName) throws CommandError {
            if (varName.equalsIgnoreCase("0") || varName.equalsIgnoreCase("air")) {
                return null;
            }

            try {
                return new ItemStack(Items.getMaterialByName(varName));
            } catch (InvalidMaterialNameException e){
                throw new TransformError(e.getMessage());
            }

        }
    }

    private static class ItemStackHandArgumentVariable implements ArgumentVariable<ItemStack> {
        private static ItemStackHandArgumentVariable instance;

        public static ItemStackHandArgumentVariable getInstance() {
            if (instance == null) {
                instance = new ItemStackHandArgumentVariable();
            }
            return instance;
        }

        @Override
        public ItemStack var(CommandSender sender, CommandArgument argument, String varName) throws CommandError {
            if (!(sender instanceof Player)) {
                throw new CommandError(Messages.CANT_AS_CONSOLE);
            }

            if (varName.equalsIgnoreCase("offhand")) {
                Player player = (Player) sender;

                if (Players.handIsEmpty(player, HandSlot.OFF_HAND)) {
                    throw new CommandError(Messages.itemInHandRequired(HandSlot.OFF_HAND));
                }

                return Players.getItemInHand(player, HandSlot.OFF_HAND);
            }

            Player player = (Player) sender;
            if (Players.handIsEmpty(player, HandSlot.MAIN_HAND)) {
                throw new CommandError(Messages.itemInHandRequired(HandSlot.MAIN_HAND));
            }

            return Players.getItemInHand(player, HandSlot.MAIN_HAND);
        }
    }
}
