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

public class StaffAnnounce implements CommandExecutor {
    private final CrankedCore plugin;
    
    public StaffAnnounce(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!plugin.getConfig().getBoolean("staff-announce-enabled"))
            return false;

        // Permission check
        if (sender instanceof Player && !sender.hasPermission("crankedcore.staffannounce")) {
            sender.sendMessage(CrankedCore.placeholderColor(plugin.getConfig().getString("no-permission-msg"), (Player)sender));
            return false;
        }

        // Usage check
        if (args.length <= 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("staff-announce-usage"))));
            return false;
        }

        // Calculate message
        args = Arrays.copyOfRange(args, 1, args.length);
        String msg = String.join(" ", args);

        // Announce to staff
        boolean playSound = !Objects.requireNonNull(plugin.getConfig().getString("staff-announce-sound")).equalsIgnoreCase("none");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("crankedcore.staffannounce.see")) {
                onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("staff-announce-format")).replace("%message%", msg)));
                if (playSound)
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(plugin.getConfig().getString("staff-announce-sound")), 1.0F, 1.0F);
            }
        }

        return true;
    }
}
