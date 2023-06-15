package io.github.thewebcode.yplugin.game.item;

import io.github.thewebcode.yplugin.game.clause.PlayerDamageEntityClause;
import io.github.thewebcode.yplugin.game.gadget.ItemGadget;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseWeapon extends ItemGadget implements Weapon {
    private Set<PlayerDamageEntityClause> damageClauses = new HashSet<>();

    private WeaponProperties properties = new WeaponProperties();

    public BaseWeapon(ItemStack item) {
        super(item);
        properties.durability(item);
    }

    public BaseWeapon(ItemBuilder builder) {
        super(builder);
        properties.durability(getItem());
    }

    @Override
    public void perform(Player holder) {
        onSwing(holder);
    }

    public void addDamageClause(PlayerDamageEntityClause... clauses) {
        Collections.addAll(damageClauses, clauses);
    }

    @Override
    public boolean canDamage(Player p, LivingEntity e) {
        for (PlayerDamageEntityClause clause : damageClauses) {
            if (!clause.canDamage(p, e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public WeaponProperties properties() {
        return properties;
    }

    @Override
    public abstract void onSwing(Player p);

    @Override
    public abstract void onActivate(Player p);

    @Override
    public abstract void onAttack(Player p, LivingEntity e);

    @Override
    public abstract void onBreak(Player p);
}
