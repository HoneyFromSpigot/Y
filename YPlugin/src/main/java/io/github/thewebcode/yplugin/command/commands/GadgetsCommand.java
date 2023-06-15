package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.game.gadget.Gadget;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.menus.gadget.GadgetsMenu;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class GadgetsCommand {

    @Command(identifier = "gadgets", permissions = Perms.COMMAND_GADGETS)
    public void onGadgetsCommand(Player player) {
        try {
            GadgetsMenu.GadgetMenu menu = GadgetsMenu.getMenu(0);
            menu.openMenu(player);
        } catch (IndexOutOfBoundsException e) {
            Chat.message(player, "&cUnable to open the gadgets menus due to an error. Do you have any gadgets registered?");
        }
    }

    @Command(identifier = "gadgets get", permissions = Perms.COMMAND_GADGETS)
    public void onGadgetsGetCommand(Player player, @Arg(name = "id") int id) {
        if (!Gadgets.isGadget(id)) {
            Chat.format(player, "&cGadget &e%s&c does not exist", id);
            return;
        }
        Gadget gadget = Gadgets.getGadget(id);
        Players.giveItem(player,gadget.getItem());
    }
}
