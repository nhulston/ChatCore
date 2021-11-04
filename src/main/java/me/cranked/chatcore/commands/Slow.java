package me.cranked.chatcore.commands;

import java.util.Set;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Slow implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public static boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!ConfigManager.getEnabled("slow-chat"))
            return false;

        // Permission check
        if (ChatCore.noPermission("chatcore.slow", sender)) {
            return false;
        }

        // Usage check
        if (args.length <= 1) {
            sender.sendMessage(ConfigManager.get("slow-usage"));
            return false;
        }

        // Parse delay
        int delay;
        try {
            delay = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ConfigManager.get("slow-usage"));
            return false;
        }

        // Set the delay
        ChatCore.setDelay(delay);

        // Args
        Set<String> arguments = Set.of(args);
        if (delay == 0) {
            if (arguments.contains("-s") && sender.hasPermission("chatcore.slow.silent")) {
                sender.sendMessage(ConfigManager.colorize(ConfigManager.get("unslow-silent").replace("%time%", Integer.toString(delay))));
            } else {
                Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("unslow").replace("%time%", Integer.toString(delay)).replace("%player%", sender.getName())));
            }
        } else if (arguments.contains("-s") && sender.hasPermission("chatcore.slow.silent")) {
            sender.sendMessage(ConfigManager.colorize(ConfigManager.get("slow-silent").replace("%time%", Integer.toString(delay))));
        } else if (arguments.contains("-a") && sender.hasPermission("chatcore.slow.anonymous")) {
            Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("slow-anon").replace("%time%", Integer.toString(delay))));
        } else {
            Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("slow").replace("%time%", Integer.toString(delay)).replace("%player%", sender.getName())));
        }

        return true;
    }
}
