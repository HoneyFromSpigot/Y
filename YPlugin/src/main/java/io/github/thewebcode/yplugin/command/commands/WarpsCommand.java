package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.menu.HelpScreen;
import io.github.thewebcode.yplugin.chat.menu.ItemFormat;
import io.github.thewebcode.yplugin.chat.menu.PageDisplay;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.config.Configuration;
import io.github.thewebcode.yplugin.inventory.menu.Menus;
import io.github.thewebcode.yplugin.menus.warpselection.WarpSelectionMenu;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.warp.Warps;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WarpsCommand {
	private Configuration config;

	public WarpsCommand() {
		config = YPlugin.getInstance().getConfiguration();
	}

	@Command(identifier = "warps", permissions = Perms.COMMAND_WARPS)
	public void onWarpsCommand(Player player, @Arg(name = "page", def = "1") int page) {
		if (config.enableWarpsMenu()) {
			WarpSelectionMenu.getMenu(page).openMenu(player);
		} else {
			HelpScreen warpsMenu = Menus.generateHelpScreen("Warps / Waypoints", PageDisplay.DEFAULT, ItemFormat.NO_DESCRIPTION, ChatColor.GREEN, ChatColor.DARK_GREEN);
			for (String warp : Warps.getWarpNames()) {
				warpsMenu.setEntry(warp, "");
			}
			warpsMenu.sendTo(player, page, 7);
		}
	}
}
