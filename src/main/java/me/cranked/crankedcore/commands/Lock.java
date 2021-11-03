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

public class Lock implements CommandExecutor {
    private final CrankedCore plugin;

    public Lock(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!ConfigManager.getEnabled("lock-chat"))
            return false;
        
        // Permission check
        if (sender instanceof Player && !sender.hasPermission("crankedcore.lock")) {
            sender.sendMessage(ConfigManager.get("no-permission"));
            return false;
        }
        
        // Lock chat
        plugin.toggleChatLocked();

        // Broadcasting
        Set<String> arguments = Set.of(args);
        // TODO bring silent messages to clear chat, etc.
        if (plugin.getChatLocked()) {
            if (arguments.contains("-s") && sender.hasPermission("crankedcore.lock.silent")) {
                sender.sendMessage(ConfigManager.get("lock-silent"));
            } else if (arguments.contains("-a") && sender.hasPermission("crankedcore.lock.anonymous")) {
                Bukkit.broadcastMessage(ConfigManager.get("lock-anon"));
            } else {
                Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("lock").replace("%player%", sender.getName())));
            }
        } else if (arguments.contains("-s") && sender.hasPermission("crankedcore.lock.silent")) {
            sender.sendMessage(ConfigManager.get("unlock-silent"));
        } else if (arguments.contains("-a") && sender.hasPermission("crankedcore.lock.anonymous")) {
            Bukkit.broadcastMessage(ConfigManager.get("unlock-anon"));
        } else {
            Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("unlock").replace("%player%", sender.getName())));
        }

        return true;
    }
}
