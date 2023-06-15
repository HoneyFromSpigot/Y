package io.github.thewebcode.yplugin.menus.warpselection;

import io.github.thewebcode.yplugin.inventory.menu.MenuItem;
import io.github.thewebcode.yplugin.warp.Warp;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.material.MaterialData;

import java.util.Arrays;

public class WarpMenuItem extends MenuItem {
    private static MaterialData warpIcon = new MaterialData(Material.COMPASS);

    private Warp warp;

    public WarpMenuItem(Warp warp) {
        super(warp.getName(), warpIcon);
        this.warp = warp;
        setDescriptions(Arrays.asList(String.format("&aTeleport to the warp &e'%s'", warp.getName())));
    }

    @Override
    public void onClick(Player player, ClickType type) {
        warp.bring(player);
        getMenu().closeMenu(player);
    }
}
