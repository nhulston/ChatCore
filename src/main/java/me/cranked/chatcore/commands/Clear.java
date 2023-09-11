package me.cranked.chatcore.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.util.FormatText;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Clear implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public static boolean command(CommandSender sender, String[] args) {
        // Return if clear chat isn't enabled
        if (!ConfigManager.getEnabled("clear-chat"))
            return false;

        // Permission check
        if (ChatCore.noPermission("chatcore.clear", sender)) {
            return false;
        }

        // Clear chat for all online players without bypass permission
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("chatcore.clear.bypass")) {
                for (int i = 0; i < 100; i++) {
                    onlinePlayer.sendMessage("");
                }
            }
        }

        Set<String> arguments = new HashSet<>(Arrays.asList(args));
        // Announce anonymous message
        if ((!arguments.contains("-s") || !sender.hasPermission("chatcore.clear.silent")) && arguments.contains("-a") && sender.hasPermission("chatcore.clear.anonymous")) {
            Bukkit.broadcastMessage(ConfigManager.get("clear-anon"));
        }
        // Announce normal message if not silent
        else if (!arguments.contains("-s") || !sender.hasPermission("chatcore.clear.silent")) {
            Bukkit.broadcastMessage(FormatText.formatText(ConfigManager.get("clear").replace("%player%", sender.getName())));
        }

        return true;
    }
}
