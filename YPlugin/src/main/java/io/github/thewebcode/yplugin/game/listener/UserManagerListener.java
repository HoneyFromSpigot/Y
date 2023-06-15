package io.github.thewebcode.yplugin.game.listener;


import io.github.thewebcode.yplugin.game.players.UserManager;
import io.github.thewebcode.yplugin.player.User;
import io.github.thewebcode.yplugin.plugin.IYBukkitPlugin;
import org.bukkit.entity.Player;

public class UserManagerListener extends AbstractUserManagerListener {

    public UserManagerListener(IYBukkitPlugin plugin, UserManager userManager) {
        super(plugin,userManager);
    }

    @Override
    public void handleJoin(Player player) {
        getUserManager().addUser(player);
    }

    @Override
    public void handleLeave(Player player) {
        User user = getUserManager().getUser(player);
        if (user == null) {
            getPlugin().getPluginLogger().severe("Unable to retrieve User data (" + getUserManager().getUserClass().getCanonicalName() + ") for player " + player.getName());
            return;
        }

        user.destroy();

        getUserManager().removeUser(player);
    }
}
