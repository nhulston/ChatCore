package me.cranked.chatcore.events;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.chatcore.ConfigManager;
import me.cranked.chatcore.ChatCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Log implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!ConfigManager.getEnabled("chat-logger"))
            return;
        String formattedMessage = ConfigManager.get("logger-format").replace("%time%", LocalTime.now().toString().substring(0, 8)).replace("%player%", e.getPlayer().getName()).replace("%message%", e.getMessage());
        formattedMessage = PlaceholderAPI.setPlaceholders(e.getPlayer(), formattedMessage);
        log(formattedMessage, LocalDate.now().toString(), "Chat Logs");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (!ConfigManager.getEnabled("command-logger"))
            return;
        String formattedMessage = ConfigManager.get("logger-format").replace("%time%", LocalTime.now().toString().substring(0, 8)).replace("%player%", e.getPlayer().getName()).replace("%message%", e.getMessage());
        formattedMessage = PlaceholderAPI.setPlaceholders(e.getPlayer(), formattedMessage);
        String command = e.getMessage();
        if (ConfigManager.getEnabled("chat-logger")) {
            List<String> chatLoggerIncludedCommands = ConfigManager.getList("chat-logger-included-commands");
            for (String includedCommand : chatLoggerIncludedCommands) {
                if (includedCommand.equalsIgnoreCase(command) || (command.length() > includedCommand.length() && (includedCommand + " ").equalsIgnoreCase(command.substring(0, includedCommand.length() + 1))))
                    log(formattedMessage, LocalDate.now().toString(), "Chat Logs");
            }
        }
        if (ConfigManager.getEnabled("command-logger")) {
            List<String> commandLoggerIncludedCommands = ConfigManager.getList("command-logger-ignored-commands");
            for (String ignoredCommand : commandLoggerIncludedCommands) {
                if (ignoredCommand.equalsIgnoreCase(command) || (command.length() > ignoredCommand.length() && (ignoredCommand + " ").equalsIgnoreCase(command.substring(0, ignoredCommand.length() + 1))))
                    return;
            }
            log(formattedMessage, LocalDate.now().toString(), "Command Logs");
        }
    }

    public static void log(String msg, String path, String folder) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ChatCore.plugin.getDataFolder().getAbsolutePath() + File.separator + folder + File.separator + path + ".txt", true))) {
            writer.write(msg);
            writer.newLine();
        } catch (IOException ignored) {}
    }
}
