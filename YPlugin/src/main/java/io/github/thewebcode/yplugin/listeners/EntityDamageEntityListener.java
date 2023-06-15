package io.github.thewebcode.yplugin.listeners;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.event.PlayerDamagePlayerEvent;
import io.github.thewebcode.yplugin.game.gadget.Gadget;
import io.github.thewebcode.yplugin.game.gadget.Gadgets;
import io.github.thewebcode.yplugin.game.item.Weapon;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.plugin.Plugins;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageEntityListener implements Listener {

    private Players playerDataHandler = null;

    public EntityDamageEntityListener() {
        playerDataHandler = YPlugin.getInstance().getPlayerHandler();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamageEntity(EntityDamageByEntityEvent e) {
        Entity attacker = e.getDamager();
        Entity attacked = e.getEntity();

        Player player = null;
        Player pAttacked = null;
        if (e.getEntityType() == EntityType.ARROW) {
            Arrow arrow = (Arrow) attacker;
            ProjectileSource source = arrow.getShooter();

            if (source == null) {
                return;
            }

            if (!(source instanceof LivingEntity)) {
                return;
            }

            LivingEntity shooter = (LivingEntity) source;
            if (shooter instanceof Player) {
                player = (Player) shooter;
            }
        }
        if (e.getEntityType() == EntityType.SNOWBALL) {
            Snowball snowball = (Snowball) attacker;
            ProjectileSource shooter = snowball.getShooter();

            if (shooter == null) {
                return;
            }

            if (!(shooter instanceof Player)) {
                return;
            }

            player = (Player) shooter;
        }
        if (attacker instanceof Player) {
            player = (Player) attacker;
        }
        if (attacked instanceof Player) {
            pAttacked = (Player) attacked;
        }
        if (pAttacked != null && playerDataHandler.getData(pAttacked).hasGodMode()) {
            e.setCancelled(true);
            return;
        }
        if (player == null || !(attacked instanceof LivingEntity)) {
            return;
        }
        if (pAttacked != null) {
            PlayerDamagePlayerEvent pvpEvent = new PlayerDamagePlayerEvent(player, pAttacked, e.getDamage(), e.getFinalDamage(), e.getCause());
            Plugins.callEvent(pvpEvent);
            if (pvpEvent.isCancelled()) {
                e.setCancelled(true);
                return;
            }
            e.setDamage(pvpEvent.getDamage());
        }

        LivingEntity entity = (LivingEntity) attacked;
        if (!Players.hasItemInHand(player)) {
            return;
        }

        ItemStack hand = player.getItemInHand();
        if (!Gadgets.isGadget(hand)) {
            return;
        }

        Gadget gadget = Gadgets.getGadget(hand);
        if (!(gadget instanceof Weapon)) {
            return;
        }

        Weapon weapon = (Weapon) gadget;
        if (!weapon.canDamage(player, entity)) {
            e.setCancelled(true);
            return;
        }
        weapon.onAttack(player, entity);
    }
}
