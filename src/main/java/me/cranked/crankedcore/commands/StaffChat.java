package me.cranked.crankedcore.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.crankedcore.CrankedCore;
import me.cranked.crankedcore.events.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChat implements CommandExecutor {
    private final CrankedCore plugin;
    public static Set<Player> staffChatList = new HashSet<>();

    public StaffChat(CrankedCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return command(sender, args);
    }

    public boolean command(CommandSender sender, String[] args) {
        // Config check
        if (!plugin.getConfig().getBoolean("staff-chat-enabled"))
            return false;

        // Permission check
        if (!sender.hasPermission("crankedcore.staffchat.send")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("no-permission-msg"))));
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
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("staff-chat-off-msg"))));
            } else {
                staffChatList.add(player);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("staff-chat-on-msg"))));
            }
        } else {
            // Calculate message
            args = Arrays.copyOfRange(args, 1, args.length);
            String msg = String.join(" ", args);
            
            // Send
            sendMessage(msg, sender);
            
            // Log in chat logger
            if (plugin.getConfig().getBoolean("chat-logger-enabled") && plugin.getConfig().getBoolean("chat-logger-staff-chat-enabled")) {
                Player player = (Player) sender;
                String formattedMessage = Objects.requireNonNull(plugin.getConfig().getString("logger-format")).replace("%time%", LocalTime.now().toString()).replace("%player%", player.getName()).replace("%message%", msg);
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
                onlinePlayer.sendMessage(CrankedCore.placeholderColor(Objects.requireNonNull(plugin.getConfig().getString("staff-chat-format")).replace("%message%", msg).replace("%player%", sender.getName()), (Player) sender));
        }
    }
}
