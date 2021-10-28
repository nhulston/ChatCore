package me.cranked.crankedcore.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import me.cranked.crankedcore.CrankedCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpy implements CommandExecutor {
    private final CrankedCore plugin;
    public static Set<Player> commandSpyList = new HashSet<>();

    public CommandSpy(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return command(sender);
    }

    public boolean command(CommandSender sender) {
        // Console check
        if (!(sender instanceof Player)) {
            System.out.println("[CrankedCore] This command is only for players."); // TODO improve logging to not sout
            return false;
        }
        
        // Config enabled check
        if (!plugin.getConfig().getBoolean("command-spy-enabled"))
            return false;
        
        // Permission check
        if (!sender.hasPermission("chat.commandspy")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("no-permission-msg"))));
            return false;
        }
        
        Player player = (Player) sender;
        // TODO check this command spy enabled on join logic
        if (commandSpyList.contains(player)) {
            commandSpyList.remove(player);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("command-spy-off-msg"))));
        } else {
            commandSpyList.add(player);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("command-spy-on-msg"))));
        }
        
        return true;
    }
}
