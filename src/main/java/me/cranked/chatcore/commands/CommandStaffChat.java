package me.cranked.chatcore.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ChatCore;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.commands.api.ChatCommand;
import me.cranked.chatcore.commands.api.CommandInfo;
import me.cranked.chatcore.events.Log;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@CommandInfo(name = "staffchat", aliases = {"staff", "staff-chat", "sc"}, permission = "chatcore.staffchat.send")
public class CommandStaffChat extends ChatCommand {
    public static Set<UUID> staffChatList = new HashSet<>();
    public CommandStaffChat() {
        setEnabled(ConfigManager.getEnabled("staff-chat"));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // Toggle staff chat
        if (args.length == 0) {
            // Console usage
            if (!(sender instanceof Player)) {
                ChatCore.plugin.getLogger().warning("Usage: /chat staff [message]]");
                return;
            }

            // Player usage
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if (staffChatList.contains(uuid)) {
                staffChatList.remove(uuid);
                sender.sendMessage(ConfigManager.get("staff-chat-off"));
            } else {
                staffChatList.add(uuid);
                sender.sendMessage(ConfigManager.get("staff-chat-on"));
            }
        } else {
            // Calculate message
            args = Arrays.copyOfRange(args, 0, args.length);
            String msg = String.join(" ", args);

            // Send
            sendMessage(msg, sender);

            // Log in chat logger
            if (ConfigManager.getEnabled("chat-logger") && ConfigManager.getEnabled("chat-logger-staff-chat")) {
                String formattedMessage = ConfigManager.colorize(ConfigManager.get("logger-format").replace("%time%", LocalTime.now().toString()).replace("%player%", sender.getName()).replace("%message%", msg));
                if (sender instanceof Player) {
                    formattedMessage = PlaceholderAPI.setPlaceholders((Player) sender, formattedMessage);
                }
                Log.log(formattedMessage, LocalDate.now().toString(), "Chat Logs");
            }
        }
    }

    public static void sendMessage(String msg, CommandSender sender) {
        msg = ConfigManager.get("staff-chat-format").replace("%message%", msg).replace("%player%", sender.getName());
        if (sender instanceof Player) {
            msg = ConfigManager.placeholderize(msg, (Player) sender);
        }
        ChatCore.plugin.getServer().broadcast(msg, "chatcore.staff.chat.see");
        ChatCore.plugin.getLogger().info(msg); //Send to Console
    }
}
