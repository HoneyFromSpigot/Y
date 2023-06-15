package io.github.thewebcode.yplugin.game.gadget;

import io.github.thewebcode.yplugin.Messages;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.item.ItemBuilder;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.time.Cooldown;
import io.github.thewebcode.yplugin.time.PlayerTicker;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class LimitedGadget extends ItemGadget {
	private int uses = 1;
	private PlayerTicker playerTicker;

	private static Cooldown delay = new Cooldown(1);

	public LimitedGadget(int uses) {
		this.uses = uses;
		playerTicker = new PlayerTicker(uses);
	}

	public LimitedGadget(ItemStack item, int uses) {
		super(item);
		playerTicker = new PlayerTicker(uses);
		this.uses = uses;
	}

	public LimitedGadget(ItemBuilder builder, int uses) {
		super(builder);
		this.uses = uses;
		playerTicker = new PlayerTicker(uses);
	}

	@Override
	public void perform(Player player) {
		if (delay.isOnCooldown(player)) {
			return;
		}
		use(player);
		playerTicker.tick(player);
		if (playerTicker.allow(player)) {
			Players.removeFromHand(player, 1);
			Chat.message(player, Messages.gadgetExpired(this));
			playerTicker.clear(player);
		}
		delay.setOnCooldown(player);
	}

	public abstract void use(Player player);

	public int getUsageLimit() {
		return uses;
	}

	public boolean canUse(Player player) {
		return delay.isOnCooldown(player);
	}

}
