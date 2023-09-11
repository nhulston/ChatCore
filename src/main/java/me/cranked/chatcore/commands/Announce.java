package me.cranked.chatcore.commands;

import java.util.Arrays;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.util.FormatText;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Announce implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return false;
    }

    // Type: warning, announce, staff-announce
    public static void command(CommandSender sender, String[] args, String type) {
        // Return if announcing isn't enabled
        if (!ConfigManager.getEnabled(type))
            return;

        // Permission check
        if (type.equals("warning")) {
            if (ChatCore.noPermission("chatcore.announce.warning", sender)) {
                return;
            }
        } else if (type.equals("announce")) {
            if (ChatCore.noPermission("chatcore.announce", sender)) {
                return;
            }
        } else {
            if (ChatCore.noPermission("chatcore.staffannounce", sender)) {
                return;
            }
        }

        // Return if incorrect usage
        if (args.length <= 1) {
            sender.sendMessage(ConfigManager.get(type + "-usage"));
            return;
        }

        // Calculate message of announcement
        args = Arrays.copyOfRange(args, 1, args.length);
        String msg = String.join(" ", args);

        // Broadcast message
        if (type.equals("staff-announce")) {
            String sound = ConfigManager.get("staff-announce-sound");
            String parsedMsg = FormatText.formatText(ConfigManager.get("staff-announce-format").replace("%message%", msg));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("chatcore.staffannounce.see")) {
                    onlinePlayer.sendMessage(parsedMsg);
                    if (!sound.equalsIgnoreCase("none"))
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
                }
            }
        } else {
            Bukkit.broadcastMessage(FormatText.formatText(ConfigManager.get(type + "-format").replace("%message%", msg)));

            String sound = ConfigManager.get(type + "-sound");
            if (!sound.equalsIgnoreCase("none")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
                }
            }
        }
    }
}
