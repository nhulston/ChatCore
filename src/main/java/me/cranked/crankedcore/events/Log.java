package me.cranked.crankedcore.events;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import me.clip.placeholderapi.PlaceholderAPI;
import me.cranked.crankedcore.CrankedCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Log implements Listener {
    private final CrankedCore plugin;

    public Log(CrankedCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        // Config check
        if (!this.plugin.getConfig().getBoolean("chat-logger-enabled"))
            return;
        String formattedMessage = Objects.requireNonNull(this.plugin.getConfig().getString("logger-format")).replace("%time%", LocalTime.now().toString()).replace("%player%", e.getPlayer().getName()).replace("%message%", e.getMessage());
        formattedMessage = PlaceholderAPI.setPlaceholders(e.getPlayer(), formattedMessage);
        log(formattedMessage, LocalDate.now().toString(), "Chat Logs");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (!this.plugin.getConfig().getBoolean("command-logger-enabled"))
            return;
        String formattedMessage = Objects.requireNonNull(this.plugin.getConfig().getString("logger-format")).replace("%time%", LocalTime.now().toString()).replace("%player%", e.getPlayer().getName()).replace("%message%", e.getMessage());
        formattedMessage = PlaceholderAPI.setPlaceholders(e.getPlayer(), formattedMessage);
        String command = e.getMessage();
        if (this.plugin.getConfig().getBoolean("chat-logger-enabled")) {
            List<String> chatLoggerIncludedCommands = this.plugin.getConfig().getStringList("chat-logger-included-commands");
            for (String includedCommand : chatLoggerIncludedCommands) {
                if (includedCommand.equalsIgnoreCase(command) || (command.length() > includedCommand.length() && (includedCommand + " ").equalsIgnoreCase(command.substring(0, includedCommand.length() + 1))))
                    log(formattedMessage, LocalDate.now().toString(), "Chat Logs");
            }
        }
        if (this.plugin.getConfig().getBoolean("command-logger-enabled")) {
            List<String> commandLoggerIncludedCommands = this.plugin.getConfig().getStringList("command-logger-ignored-commands");
            for (String ignoredCommand : commandLoggerIncludedCommands) {
                if (ignoredCommand.equalsIgnoreCase(command) || (command.length() > ignoredCommand.length() && (ignoredCommand + " ").equalsIgnoreCase(command.substring(0, ignoredCommand.length() + 1))))
                    return;
            }
            log(formattedMessage, LocalDate.now().toString(), "Command Logs");
        }
    }

    // TODO don't create this writer every time?
    public void log(String msg, String path, String folder) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.plugin.getDataFolder().getAbsolutePath() + File.separator + folder + File.separator + path + ".txt", true))) {
            writer.write(msg);
            writer.newLine();
        } catch (IOException ignored) {}
    }
}
