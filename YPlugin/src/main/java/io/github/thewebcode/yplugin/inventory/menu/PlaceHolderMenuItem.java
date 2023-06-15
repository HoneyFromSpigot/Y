package io.github.thewebcode.yplugin.inventory.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.material.MaterialData;

public class PlaceHolderMenuItem extends MenuItem {

	public PlaceHolderMenuItem(String title, String description) {
		super(title, new MaterialData(Material.PAPER));
		setDescriptions(description);
	}

	@Override
	public void onClick(Player player, ClickType type) {

	}
}
