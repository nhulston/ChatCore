package me.cranked.crankedcore.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Slow implements CommandExecutor {
    private final CrankedCore plugin;

    public Slow(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!plugin.getConfig().getBoolean("slow-enabled"))
            return false;

        // Permission check
        if (sender instanceof Player && !sender.hasPermission("crankedcore.slow")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.<String>requireNonNull(plugin.getConfig().getString("no-permission-msg"))));
            return false;
        }

        // Usage check
        if (args.length <= 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("slow-usage-msg"))));
            return false;
        }

        // Parse delay
        int delay;
        try {
            delay = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("slow-usage-msg"))));
            return false;
        }

        // Set the delay
        plugin.setDelay(delay);

        // Args
        Set<String> arguments = Set.of(args);
        if (delay == 0) {
            if (arguments.contains("-s") && sender.hasPermission("crankedcore.slow.silent")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("slow-silent-msg-0")).replace("%time%", Integer.toString(delay))));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("slow-msg-0")).replace("%time%", Integer.toString(delay)).replace("%player%", sender.getName())));
            }
        } else if (arguments.contains("-s") && sender.hasPermission("crankedcore.slow.silent")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("slow-silent-msg")).replace("%time%", Integer.toString(delay))));
        } else if (arguments.contains("-a") && sender.hasPermission("crankedcore.slow.anonymous")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("slow-msg-anon")).replace("%time%", Integer.toString(delay))));
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("slow-msg")).replace("%time%", Integer.toString(delay)).replace("%player%", sender.getName())));
        }

        return true;
    }
}
