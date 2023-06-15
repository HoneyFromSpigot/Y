package io.github.thewebcode.yplugin.game.guns;

import io.github.thewebcode.yplugin.game.gadget.Gadget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface Gun extends Gadget {

    void damage(LivingEntity damaged, Player shooter);

    void onFire(Player shooter);

    BulletActions getBulletActions();

    void setBulletActions(BulletActions actions);

    GunProperties properties();

    BulletProperties bulletProperties();

    void properties(GunProperties attributes);

    boolean reload(Player player);

    boolean needsReload(Player player);

    double damage();
}
