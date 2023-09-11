package me.cranked.chatcore.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.events.Log;
import me.cranked.chatcore.util.FormatText;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffChat implements CommandExecutor {
    public static final Set<Player> staffChatList = new HashSet<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public static boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!ConfigManager.getEnabled("staff-chat"))
            return false;

        // Permission check
        if (ChatCore.noPermission("chatcore.staffchat.send", sender)) {
            return false;
        }

        // Toggle staff chat
        if (args.length <= 1) {
            // Console usage
            if (!(sender instanceof Player)) {
                Bukkit.getLogger().warning("[ChatCore] Usage: /chat staff [message]]");
                return false;
            }

            // Player usage
            Player player = (Player) sender;
            if (staffChatList.contains(player)) {
                staffChatList.remove(player);
                sender.sendMessage(ConfigManager.get("staff-chat-off"));
            } else {
                staffChatList.add(player);
                sender.sendMessage(ConfigManager.get("staff-chat-on"));
            }
        } else {
            // Calculate message
            args = Arrays.copyOfRange(args, 1, args.length);
            String msg = String.join(" ", args);
            
            // Send
            sendMessage(msg, sender);
            
            // Log in chat logger
            if (ConfigManager.getEnabled("chat-logger") && ConfigManager.getEnabled("chat-logger-staff-chat")) {
                String formattedMessage = FormatText.formatText(ConfigManager.get("logger-format").replace("%time%", LocalTime.now().toString()).replace("%player%", sender.getName()).replace("%message%", msg));
                if (sender instanceof Player) {
                    formattedMessage = PlaceholderAPI.setPlaceholders((Player) sender, formattedMessage);
                }
                Log.log(formattedMessage, LocalDate.now().toString(), "Chat Logs");
            }
        }

        return true;
    }

    public static void sendMessage(String msg, CommandSender sender) {
        msg = ConfigManager.get("staff-chat-format").replace("%message%", msg).replace("%player%", sender.getName());
        if (sender instanceof Player) {
            msg = FormatText.placeholderize(msg, (Player) sender);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("chatcore.staffchat.see")) {
                player.sendMessage(msg);
            }
        }
    }
}
