package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.menus.confirmation.ConfirmationMenu;
import io.github.thewebcode.yplugin.permission.Perms;
import io.github.thewebcode.yplugin.player.MinecraftPlayer;
import io.github.thewebcode.yplugin.player.Players;
import org.bukkit.entity.Player;

public class TeleportRequestCommand {
    private static YPlugin commons = YPlugin.getInstance();

    @Command(identifier = "tpa", permissions = Perms.COMMAND_TPA)
    public void onTpaCommand(Player player, @Arg(name = "target") Player target) {
        if (YPlugin.TeleportMenuSettings.getInstance().hasMenuDisabled(target.getUniqueId())) {
            commons.getPlayerHandler().getData(player).requestTeleportTo(target);
        } else {
            ConfirmationMenu.of(String.format("Teleport %s to you?", player.getName()))
                    .onConfirm((menu, p) -> {
                        Players.teleport(player, p);
                        menu.closeMenu(p);
                    }).onDeny((menu, p) -> {
                Chat.message(player, String.format("&c%s&e has denied your teleport request", p.getName()));
                menu.closeMenu(p);
            }).exitOnClickOutside(true)
                    .onOpen((menu, p) -> {
                        commons.getPlayerHandler().getData(p).setGodMode(true);
                    }).onClose((menu, p) -> {
                commons.getPlayerHandler().getData(p).setGodMode(false);
            }).openMenu(target);
        }
    }

    @Command(identifier = "tpahere", permissions = Perms.COMMAND_TPA)
    public void onTpaHereCommand(Player player, @Arg(name = "target") Player target) {
        if (YPlugin.TeleportMenuSettings.getInstance().hasMenuDisabled(target.getUniqueId())) {
            commons.getPlayerHandler().getData(player).requestTeleportHere(target);
        } else {
            ConfirmationMenu.of(String.format("Teleport to %s?", player.getName()))
                    .onConfirm((menu, p) -> {
                        Players.teleport(p, player);
                        menu.closeMenu(p);
                    }).onDeny((menu, p) -> {
                Chat.message(player, String.format("&c%s&e has denied your teleport request", p.getName()));
                menu.closeMenu(p);
            }).exitOnClickOutside(true)
                    .onOpen((menu, p) -> {
                        commons.getPlayerHandler().getData(p).setGodMode(true);
                    }).onClose((menu, p) -> {
                commons.getPlayerHandler().getData(p).setGodMode(false);
            }).openMenu(target);
        }
    }

    @Command(identifier = "tpaccept", permissions = Perms.COMMAND_TPA)
    public void onTpAcceptCommand(Player player) {
        MinecraftPlayer acceptor = commons.getPlayerHandler().getData(player);
        if (!acceptor.hasTeleportRequest()) {
            Chat.message(player, "&eYou do &cnot&e have any teleport requests");
            return;
        }

        acceptor.acceptTeleport();
    }

    @Command(identifier = "tpdeny", permissions = Perms.COMMAND_TPA)
    public void onTpDenyCommand(Player player) {
        MinecraftPlayer denier = commons.getPlayerHandler().getData(player);

        if (!denier.hasTeleportRequest()) {
            Chat.message(player, "&eYou do &cnot&e have any teleport requests");
            return;
        }
        denier.denyTeleport();
    }
}
