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

public class Warning implements CommandExecutor {
    private final CrankedCore plugin;

    public Warning(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!this.plugin.getConfig().getBoolean("warning-enabled"))
            return false;

        // Permission check TODO fix duplicate code
        if (sender instanceof Player && !sender.hasPermission("crankedcore.announce.warning")) {
            sender.sendMessage(ConfigManager.get("no-permission"));
            return false;
        }

        // Usage check TODO fix duplicate code
        if (args.length <= 1) {
            sender.sendMessage(ConfigManager.get("warning-usage"));
            return false;
        }

        // Calculate message TODO fix duplicate code
        args = Arrays.copyOfRange(args, 1, args.length);
        String msg = String.join(" ", args);
        Bukkit.broadcastMessage(ConfigManager.colorize(ConfigManager.get("warning-format").replace("%message%", msg)));

        // Play sound
        String sound = ConfigManager.get("warning-sound");
        if (!sound.equalsIgnoreCase("none")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
            }
        }

        return true;
    }
}
