package me.cranked.chatcore.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.events.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DonatorChat implements CommandExecutor {
    public static Set<Player> donatorChatList = new HashSet<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public static boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!ConfigManager.getEnabled("donator-chat"))
            return false;

        // Permission check
        if (ChatCore.noPermission("chatcore.donatorchat.send", sender)) {
            return false;
        }

        // Toggle donator chat
        if (args.length <= 1) {
            // Console usage
            if (!(sender instanceof Player)) {
                Bukkit.getLogger().warning("[ChatCore] Usage: /chat donator [message]]");
                return false;
            }

            // Player usage
            Player player = (Player) sender;
            if (donatorChatList.contains(player)) {
                donatorChatList.remove(player);
                sender.sendMessage(ConfigManager.get("donator-chat-off"));
            } else {
                donatorChatList.add(player);
                sender.sendMessage(ConfigManager.get("donator-chat-on"));
            }
        } else {
            // Calculate message
            args = Arrays.copyOfRange(args, 1, args.length);
            String msg = String.join(" ", args);
            
            // Send
            sendMessage(msg, sender);
            
            // Log in chat logger
            if (ConfigManager.getEnabled("chat-logger") && ConfigManager.getEnabled("chat-logger-donator-chat")) {
                String formattedMessage = ConfigManager.colorize(ConfigManager.get("logger-format").replace("%time%", LocalTime.now().toString()).replace("%player%", sender.getName()).replace("%message%", msg));
                if (sender instanceof Player) {
                    formattedMessage = PlaceholderAPI.setPlaceholders((Player) sender, formattedMessage);
                }
                Log.log(formattedMessage, LocalDate.now().toString(), "Chat Logs");
            }
        }

        return true;
    }

    public static void sendMessage(String msg, CommandSender sender) {
        msg = ConfigManager.get("donator-chat-format").replace("%message%", msg).replace("%player%", sender.getName());
        if (sender instanceof Player) {
            msg = ConfigManager.placeholderize(msg, (Player) sender);
        }
        Bukkit.broadcast(msg, "chatcore.donatorchat.see");
    }
}
