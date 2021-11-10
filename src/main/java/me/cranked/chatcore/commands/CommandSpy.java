package me.cranked.chatcore.commands;

import java.util.HashSet;
import java.util.Set;
import me.cranked.chatcore.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class CommandSpy implements CommandExecutor {
    public static Set<Player> commandSpyList = new HashSet<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender);
    }

    public static boolean command(CommandSender sender) {
        // Console check
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().warning("[ChatCore] This command is only for players.");
            return false;
        }
        
        // Config enabled check
        if (!ConfigManager.getEnabled("command-spy"))
            return false;
        
        // Permission check
        if (!sender.hasPermission("chat.commandspy")) {
            sender.sendMessage(ConfigManager.get("no-permission"));
            return false;
        }
        
        Player player = (Player) sender;
        if (commandSpyList.contains(player)) {
            commandSpyList.remove(player);
            sender.sendMessage(ConfigManager.get("command-spy-off"));
        } else {
            commandSpyList.add(player);
            sender.sendMessage(ConfigManager.get("command-spy-on"));
        }
        
        return true;
    }
}
