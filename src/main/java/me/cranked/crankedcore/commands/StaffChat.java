package me.cranked.crankedcore.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.crankedcore.ConfigManager;
import me.cranked.crankedcore.CrankedCore;
import me.cranked.crankedcore.events.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffChat implements CommandExecutor {
    private final CrankedCore plugin;
    public static Set<Player> staffChatList = new HashSet<>();

    public StaffChat(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!ConfigManager.getEnabled("staff-chat"))
            return false;

        // Permission check
        if (!sender.hasPermission("crankedcore.staffchat.send")) {
            sender.sendMessage(ConfigManager.get("no-permission"));
            return false;
        }

        // Toggle staff chat
        if (args.length <= 1) {
            // Console usage
            if (!(sender instanceof Player)) {
                System.out.println("Usage: /sc [message]]");
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
                Player player = (Player) sender;
                String formattedMessage = ConfigManager.colorize(ConfigManager.get("logger-format").replace("%time%", LocalTime.now().toString()).replace("%player%", player.getName()).replace("%message%", msg));
                formattedMessage = PlaceholderAPI.setPlaceholders(player, formattedMessage);
                Log logger = new Log(plugin);
                logger.log(formattedMessage, LocalDate.now().toString(), "Chat Logs");
            }
        }

        return true;
    }

    public void sendMessage(String msg, CommandSender sender) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("crankedcore.staffchat.see")) // TODO what is this placeholder color
                onlinePlayer.sendMessage(CrankedCore.placeholderColor(ConfigManager.get("staff-chat-format").replace("%message%", msg).replace("%player%", sender.getName()), (Player) sender));
        }
    }
}
