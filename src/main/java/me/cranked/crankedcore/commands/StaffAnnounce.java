package me.cranked.crankedcore.commands;

import java.util.Arrays;
import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffAnnounce implements CommandExecutor {
    private final CrankedCore plugin;
    
    public StaffAnnounce(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!plugin.getConfig().getBoolean("staff-announce-enabled"))
            return false;

        // Permission check
        if (sender instanceof Player && !sender.hasPermission("crankedcore.staffannounce")) {
            sender.sendMessage(CrankedCore.placeholderColor(plugin.getConfig().getString("no-permission"), (Player)sender));
            return false;
        }

        // Usage check
        if (args.length <= 1) {
            sender.sendMessage(ConfigManager.get("staff-announce-usage"));
            return false;
        }

        // Calculate message
        args = Arrays.copyOfRange(args, 1, args.length);
        String msg = String.join(" ", args);

        // Announce to staff
        String sound = ConfigManager.get("staff-announce-sound");
        String parsedMsg = ConfigManager.colorize(ConfigManager.get("staff-announce-format").replace("%message%", msg));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("crankedcore.staffannounce.see")) {
                onlinePlayer.sendMessage(parsedMsg);
                if (!sound.equalsIgnoreCase("none"))
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
            }
        }

        return true;
    }
}
