package io.github.thewebcode.yplugin.debug.actions;

import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.debug.DebugAction;
import io.github.thewebcode.yplugin.plugin.Plugins;
import org.bukkit.entity.Player;

public class DebugNmsVersion implements DebugAction {
	@Override
	public void doAction(Player player, String... args) {
		Chat.message(player, String.format("&7Minecraft NMS Version is &l%s", Plugins.getNmsVersion()));
	}

	@Override
	public String getActionName() {
		return "debug_nms_version";
	}
}
