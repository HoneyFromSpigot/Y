package io.github.thewebcode.yplugin.game.item;

import io.github.thewebcode.yplugin.game.gadget.Gadget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface Weapon extends Gadget {

    @Override
    default void perform(Player holder) {
        onSwing(holder);
    }

    void onAttack(Player p, LivingEntity e);

    void onSwing(Player p);


    void onActivate(Player p);

    boolean canDamage(Player p, LivingEntity e);

    WeaponProperties properties();
}
