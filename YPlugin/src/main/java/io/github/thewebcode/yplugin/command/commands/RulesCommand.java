package io.github.thewebcode.yplugin.command.commands;

import io.github.thewebcode.yplugin.YPlugin;
import io.github.thewebcode.yplugin.chat.Chat;
import io.github.thewebcode.yplugin.command.Arg;
import io.github.thewebcode.yplugin.command.Command;
import io.github.thewebcode.yplugin.command.Wildcard;
import org.bukkit.entity.Player;

import java.util.List;

public class RulesCommand {

    @Command(identifier = "rules", description = "View the rules of the server. Obey, or bite the dust :)")
    public void onRulesCommand(Player player) {
        List<String> rules = YPlugin.Rules.getRules();

        for (String rule : rules) {
            Chat.message(player, String.format("%s%s", "&c&l", rule));
        }
    }

    @Command(identifier = "rules add", description = "Add a new rule to the list of rules", permissions="commons.commands.rules.add")
    public void onRulesAddCommand(Player player, @Wildcard @Arg(name = "rule") String rule) {
        YPlugin.Rules.add(rule);
        Chat.actionMessage(player, String.format("&eRule Added: &a%s", rule));
    }

    @Command(identifier = "rules reload", description = "Reload the rules from file",permissions="commons.commands.rules.reload")
    public void onRulesReloadCommand(Player player) {
        YPlugin.Rules.load();
    }
}
