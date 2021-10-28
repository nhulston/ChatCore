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

public class Warning implements CommandExecutor {
    private final CrankedCore plugin;

    public Warning(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!this.plugin.getConfig().getBoolean("warning-enabled"))
            return false;

        // Permission check TODO fix duplicate code
        if (sender instanceof Player && !sender.hasPermission("crankedcore.announce.warning")) {
            sender.sendMessage(CrankedCore.placeholderColor(this.plugin.getConfig().getString("no-permission-msg"), (Player) sender));
            return false;
        }

        // Usage check TODO fix duplicate code
        if (args.length <= 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("warning-usage"))));
            return false;
        }

        // Calculate message TODO fix duplicate code
        args = Arrays.copyOfRange(args, 1, args.length);
        String msg = String.join(" ", args);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.plugin.getConfig().getString("warning-format")).replace("%message%", msg)));

        // Play sound
        if (!Objects.requireNonNull(this.plugin.getConfig().getString("warning-sound")).equalsIgnoreCase("none")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(this.plugin.getConfig().getString("warning-sound")), 1.0F, 1.0F);
            }
        }

        return true;
    }
}
