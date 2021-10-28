package me.cranked.crankedcore.commands;

import java.util.Arrays;
import java.util.Objects;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Announce implements CommandExecutor {
    private final CrankedCore plugin;

    public Announce(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Return if announcing isn't enabled
        if (!plugin.getConfig().getBoolean("announce-enabled"))
            return false;

        // TODO check if translate alternate necessary? or just use §
        // TODO initialize plugin.getConfig strings, store as static variables, reference them here
        // Return if player doesn't have permission
        if (sender instanceof Player && !sender.hasPermission("crankedcore.announce")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("no-permission-msg"))));
            return false;
        }

        // Return if incorrect usage
        if (args.length <= 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("announce-usage"))));
            return false;
        }

        // Calculate message of announcement
        args = Arrays.copyOfRange(args, 1, args.length);
        String msg = String.join(" ", args);

        // Broadcast message
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("announce-format")).replace("%message%", msg)));

        // Play sound to all players
        if (!Objects.requireNonNull(plugin.getConfig().getString("announce-sound")).equalsIgnoreCase("none")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(plugin.getConfig().getString("announce-sound")), 1.0F, 1.0F);
            }
        }
        return true;
    }
}
