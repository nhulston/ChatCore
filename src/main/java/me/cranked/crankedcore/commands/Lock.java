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

public class Lock implements CommandExecutor {
    private final CrankedCore plugin;

    public Lock(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!plugin.getConfig().getBoolean("lock-enabled"))
            return false;
        
        // Permission check
        if (sender instanceof Player && !sender.hasPermission("crankedcore.lock")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("no-permission-msg"))));
            return false;
        }
        
        // Lock chat
        plugin.toggleChatLocked();

        // Broadcasting
        Set<String> arguments = Set.of(args);
        // TODO bring silent messages to clear chat, etc.
        if (plugin.getChatLocked()) {
            if (arguments.contains("-s") && sender.hasPermission("crankedcore.lock.silent")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("lock-silent-msg"))));
            } else if (arguments.contains("-a") && sender.hasPermission("crankedcore.lock.anonymous")) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("lock-msg-anon"))));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("lock-msg")).replace("%player%", sender.getName())));
            }
        } else if (arguments.contains("-s") && sender.hasPermission("crankedcore.lock.silent")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("unlock-silent-msg"))));
        } else if (arguments.contains("-a") && sender.hasPermission("crankedcore.lock.anonymous")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("unlock-msg-anon"))));
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("unlock-msg")).replace("%player%", sender.getName())));
        }

        return true;
    }
}
