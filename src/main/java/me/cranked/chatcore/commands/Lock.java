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
import org.jetbrains.annotations.NotNull;

public class Lock implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public static boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!ConfigManager.getEnabled("lock-chat"))
            return false;

        // Permission check
        if (ChatCore.noPermission("chatcore.lock", sender)) {
            return false;
        }
        
        // Lock chat
        ChatCore.toggleChatLocked();

        // Broadcasting
        Set<String> arguments = new HashSet<>(Arrays.asList(args));
        if (ChatCore.getChatLocked()) {
            if (arguments.contains("-s") && sender.hasPermission("chatcore.lock.silent")) {
                sender.sendMessage(ConfigManager.get("lock-silent"));
            } else if (arguments.contains("-a") && sender.hasPermission("chatcore.lock.anonymous")) {
                Bukkit.broadcastMessage(ConfigManager.get("lock-anon"));
            } else {
                Bukkit.broadcastMessage(FormatText.formatText(ConfigManager.get("lock").replace("%player%", sender.getName())));
            }
        } else if (arguments.contains("-s") && sender.hasPermission("chatcore.lock.silent")) {
            sender.sendMessage(ConfigManager.get("unlock-silent"));
        } else if (arguments.contains("-a") && sender.hasPermission("chatcore.lock.anonymous")) {
            Bukkit.broadcastMessage(ConfigManager.get("unlock-anon"));
        } else {
            Bukkit.broadcastMessage(FormatText.formatText(ConfigManager.get("unlock").replace("%player%", sender.getName())));
        }

        return true;
    }
}
