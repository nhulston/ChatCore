package me.cranked.crankedcore.commands;

import java.util.Set;
import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Clear implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Return if clear chat isn't enabled
        if (!ConfigManager.getEnabled("clear-chat"))
            return false;

        // Return if player doesn't have permission
        if (sender instanceof Player && !sender.hasPermission("crankedcore.clear")) {
            sender.sendMessage(ConfigManager.get("no-permission"));
            return false;
        }

        // Clear chat for all online players without bypass permission
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("crankedcore.clear.bypass"))
                for (int i = 0; i < 100; i++)
                    onlinePlayer.sendMessage("");
        }

        Set<String> arguments = Set.of(args);
        // Announce anonymous message
        if ((!arguments.contains("-s") || !sender.hasPermission("crankedcore.clear.silent")) && arguments.contains("-a") && sender.hasPermission("crankedcore.clear.anonymous")) {
            Bukkit.broadcastMessage(ConfigManager.get("clear-anon"));
        }
        // Announce normal message if not silent
        else if (!arguments.contains("-s") || !sender.hasPermission("crankedcore.clear.silent")) {
            Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("clear").replace("%player%", sender.getName())));
        }

        return true;
    }
}
